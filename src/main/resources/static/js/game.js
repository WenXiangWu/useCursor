let stompClient = null;
let currentPlayer = null;
let currentGame = null;

function connect() {
    const socket = new SockJS('/poker-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        console.log('已连接到WebSocket服务器');
        
        // 订阅游戏桌列表更新
        stompClient.subscribe('/topic/tables', function(response) {
            const tables = JSON.parse(response.body);
            updateTableList(tables);
        });
        
        // 获取当前游戏桌列表
        $.get('/api/tables', function(tables) {
            updateTableList(tables);
        });
    });
}

function login() {
    const username = $('#username').val();
    const password = $('#password').val();
    
    $.post('/api/auth/login', {
        username: username,
        password: password
    }, function(response) {
        if (response.success) {
            currentPlayer = response.player;
            $('#playerInfo').text(`玩家: ${currentPlayer.username} | 筹码: ${currentPlayer.chips}`);
            $('#loginPanel').hide();
            $('#lobbyPanel').show();
            connect();
        } else {
            alert('登录失败: ' + response.message);
        }
    });
}

function createTable() {
    const config = {
        smallBlind: 10,
        bigBlind: 20
    };
    
    stompClient.send("/app/table/create", {}, JSON.stringify(config));
}

function updateTableList(tables) {
    const tableList = $('#tableList');
    tableList.empty();
    
    tables.forEach(table => {
        const tableElement = $(`
            <div class="col-md-4 mb-3">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">游戏桌 ${table.id}</h5>
                        <p class="card-text">
                            玩家数: ${table.players.length}/${table.config.maxPlayers}<br>
                            小盲/大盲: ${table.config.smallBlind}/${table.config.bigBlind}
                        </p>
                        <button onclick="joinTable('${table.id}')" class="btn btn-primary">加入游戏</button>
                    </div>
                </div>
            </div>
        `);
        tableList.append(tableElement);
    });
}

function joinTable(tableId) {
    if (!currentPlayer) {
        alert('请先登录');
        return;
    }
    
    stompClient.send("/app/game.join", {}, JSON.stringify({
        gameId: tableId,
        player: currentPlayer
    }));
    
    // 订阅该游戏桌的更新
    stompClient.subscribe(`/topic/game/${tableId}/state`, function(response) {
        const gameState = JSON.parse(response.body);
        updateGameState(gameState);
    });
    
    $('#lobbyPanel').hide();
    $('#gamePanel').show();
    $('#tableId').text(tableId);
}

function updateGameState(gameState) {
    currentGame = gameState;
    
    // 更新公共牌
    const communityCards = $('#communityCards');
    communityCards.empty();
    gameState.communityCards.forEach(card => {
        communityCards.append(`<div class="card">${card}</div>`);
    });
    
    // 更新底池
    $('#potAmount').text(gameState.pot);
    
    // 更新玩家座位
    const playerSeats = $('#playerSeats');
    playerSeats.empty();
    gameState.players.forEach(player => {
        const isCurrentPlayer = player.id === currentPlayer.id;
        const isCurrentTurn = player.id === gameState.currentPlayer;
        
        const playerElement = $(`
            <div class="col-md-3">
                <div class="player-seat ${isCurrentPlayer ? 'active' : ''} ${isCurrentTurn ? 'current-turn' : ''}">
                    <div>${player.username}</div>
                    <div class="player-chips">筹码: ${player.chips}</div>
                    <div class="player-bet">当前下注: ${player.currentBet}</div>
                    ${isCurrentPlayer ? `
                        <div class="mt-2">
                            <div class="card ${player.cards[0] ? '' : 'hidden'}">${player.cards[0] || '?'}</div>
                            <div class="card ${player.cards[1] ? '' : 'hidden'}">${player.cards[1] || '?'}</div>
                        </div>
                    ` : ''}
                </div>
            </div>
        `);
        playerSeats.append(playerElement);
    });
    
    // 更新操作面板
    const actionPanel = $('#actionPanel');
    if (gameState.currentPlayer === currentPlayer.id) {
        actionPanel.show();
        // 根据当前可执行的操作启用/禁用按钮
        updateActionButtons(gameState);
    } else {
        actionPanel.hide();
    }
}

function updateActionButtons(gameState) {
    const currentBet = gameState.currentBet;
    const playerBet = gameState.players.find(p => p.id === currentPlayer.id).currentBet;
    
    // 启用/禁用让牌按钮
    $('button[onclick="playerAction(\'check\')"]').prop('disabled', currentBet > playerBet);
    
    // 设置加注输入框的最小值
    $('#raiseAmount').attr('min', currentBet * 2);
}

function playerAction(action) {
    if (!currentGame || !currentPlayer) return;
    
    const amount = action === 'raise' ? parseInt($('#raiseAmount').val()) : null;
    
    stompClient.send("/app/game.action", {}, JSON.stringify({
        gameId: currentGame.id,
        playerId: currentPlayer.id,
        action: action,
        amount: amount
    }));
}

function leaveGame() {
    if (!currentGame || !currentPlayer) return;
    
    stompClient.send("/app/game.leave", {}, JSON.stringify({
        gameId: currentGame.id,
        playerId: currentPlayer.id
    }));
    
    $('#gamePanel').hide();
    $('#lobbyPanel').show();
    currentGame = null;
}

// 页面加载完成后连接WebSocket
$(document).ready(function() {
    // 初始显示登录面板
    $('#loginPanel').show();
    $('#lobbyPanel').hide();
    $('#gamePanel').hide();
}); 