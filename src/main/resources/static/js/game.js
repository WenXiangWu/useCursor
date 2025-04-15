// 全局变量
let stompClient = null;
let currentPlayer = null;
let currentGame = null;
let currentActionPlayer = null;
let dealer = {
    name: "荷官",
    isDealing: false,
    currentAction: "等待开始新一轮"
};

// 模拟玩家数据
const mockPlayers = [
    { username: "玩家1", chips: 1000, cards: [], currentBet: 0, active: true, position: 1, role: "庄家(D)" },
    { username: "玩家2", chips: 1000, cards: [], currentBet: 0, active: true, position: 2, role: "小盲注(SB)" },
    { username: "玩家3", chips: 1000, cards: [], currentBet: 0, active: true, position: 3, role: "大盲注(BB)" },
    { username: "玩家4", chips: 1000, cards: [], currentBet: 0, active: true, position: 4, role: "" },
    { username: "玩家5", chips: 1000, cards: [], currentBet: 0, active: true, position: 5, role: "" }
];

// 扑克牌花色和数字
const suits = ['♠', '♥', '♦', '♣'];
const ranks = ['2', '3', '4', '5', '6', '7', '8', '9', '10', 'J', 'Q', 'K', 'A'];
let deck = [];

// 页面加载完成后初始化
document.addEventListener('DOMContentLoaded', function() {
    console.log("页面加载完成，开始初始化...");
    initializeGame();
});

function initializeGame() {
    // 检查登录状态
    const token = localStorage.getItem('token');
    const username = localStorage.getItem('username');
    
    if (!token || !username) {
        window.location.href = '/index.html';
        return;
    }

    console.log("初始化游戏...");
    
    // 显示当前玩家信息
    document.getElementById('currentPlayer').textContent = username;
    currentPlayer = { username: username, chips: 1000 };
    
    // 绑定所有按钮事件
    bindEvents();
    
    // 初始化游戏状态
    initializeGameState();
}

function bindEvents() {
    console.log("绑定事件处理程序...");
    
    // 开始新一轮按钮
    const startNewRoundBtn = document.getElementById('startNewRound');
    if (startNewRoundBtn) {
        startNewRoundBtn.addEventListener('click', () => {
            console.log("点击了开始新一轮按钮");
            startNewRound();
        });
    }

    // 看牌/跟注按钮
    const checkCallBtn = document.getElementById('checkCall');
    if (checkCallBtn) {
        checkCallBtn.addEventListener('click', () => {
            console.log("点击了看牌/跟注按钮");
            handleAction('check');
        });
    }

    // 加注按钮和滑块
    const raiseBtn = document.getElementById('raise');
    const raiseControls = document.querySelector('.raise-controls');
    const raiseAmount = document.getElementById('raiseAmount');
    const raiseValue = document.getElementById('raiseValue');

    if (raiseBtn && raiseControls && raiseAmount && raiseValue) {
        raiseBtn.addEventListener('click', () => {
            console.log("点击了加注按钮");
            raiseControls.style.display = raiseControls.style.display === 'none' ? 'block' : 'none';
        });

        raiseAmount.addEventListener('input', (e) => {
            raiseValue.textContent = e.target.value;
        });

        raiseAmount.addEventListener('change', (e) => {
            handleAction('raise', { amount: parseInt(e.target.value) });
            raiseControls.style.display = 'none';
        });
    }

    // 弃牌按钮
    const foldBtn = document.getElementById('fold');
    if (foldBtn) {
        foldBtn.addEventListener('click', () => {
            console.log("点击了弃牌按钮");
            handleAction('fold');
        });
    }

    // 离开游戏按钮
    const leaveGameBtn = document.getElementById('leaveGame');
    if (leaveGameBtn) {
        leaveGameBtn.addEventListener('click', () => {
            console.log("点击了离开游戏按钮");
            handleLeaveGame();
        });
    }

    // 模拟玩家操作按钮
    const simCheckCallBtn = document.getElementById('simCheckCall');
    const simRaiseBtn = document.getElementById('simRaise');
    const simFoldBtn = document.getElementById('simFold');
    const simulatedPlayerSelect = document.getElementById('simulatedPlayer');

    if (simCheckCallBtn && simulatedPlayerSelect) {
        simCheckCallBtn.addEventListener('click', () => {
            const selectedPlayer = simulatedPlayerSelect.value;
            if (selectedPlayer) {
                handleSimulatedAction(selectedPlayer, 'check');
            } else {
                showError('请先选择要模拟的玩家');
            }
        });
    }

    if (simRaiseBtn && simulatedPlayerSelect) {
        simRaiseBtn.addEventListener('click', () => {
            const selectedPlayer = simulatedPlayerSelect.value;
            if (selectedPlayer) {
                handleSimulatedAction(selectedPlayer, 'raise', { amount: 50 });
            } else {
                showError('请先选择要模拟的玩家');
            }
        });
    }

    if (simFoldBtn && simulatedPlayerSelect) {
        simFoldBtn.addEventListener('click', () => {
            const selectedPlayer = simulatedPlayerSelect.value;
            if (selectedPlayer) {
                handleSimulatedAction(selectedPlayer, 'fold');
            } else {
                showError('请先选择要模拟的玩家');
            }
        });
    }
}

function initializeGameState() {
    console.log("初始化游戏状态...");
    updateGameState({
        players: mockPlayers,
        communityCards: [],
        pot: 0,
        dealer: dealer
    });
}

function initializeDeck() {
    console.log("初始化扑克牌...");
    deck = [];
    for (let suit of suits) {
        for (let rank of ranks) {
            deck.push(suit + rank);
        }
    }
    // 洗牌
    for (let i = deck.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [deck[i], deck[j]] = [deck[j], deck[i]];
    }
}

function startNewRound() {
    console.log("开始新一轮...");
    initializeDeck();
    dealer.isDealing = true;
    dealer.currentAction = "开始发牌";
    
    // 轮换位置
    rotatePositions();
    
    // 清空所有玩家手牌和下注
    mockPlayers.forEach(player => {
        player.cards = [];
        player.currentBet = 0;
        player.active = true;
    });
    
    // 设置初始行动玩家（大盲注后面的玩家）
    currentActionPlayer = getNextActivePlayer(getBigBlindPlayer());
    
    // 更新界面显示
    updateGameState({
        players: mockPlayers,
        communityCards: [],
        pot: 0,
        dealer: dealer
    });
    
    // 模拟发牌过程
    dealCards();
}

function dealCards() {
    console.log("开始发牌...");
    // 第一轮发牌
    setTimeout(() => {
        dealer.currentAction = "第一轮发牌";
        mockPlayers.forEach(player => {
            player.cards.push(deck.pop());
        });
        updateGameState({
            players: mockPlayers,
            communityCards: [],
            pot: 0,
            dealer: dealer
        });
        
        // 第二轮发牌
        setTimeout(() => {
            dealer.currentAction = "第二轮发牌";
            mockPlayers.forEach(player => {
                player.cards.push(deck.pop());
            });
            updateGameState({
                players: mockPlayers,
                communityCards: [],
                pot: 0,
                dealer: dealer
            });
            
            // 发公共牌
            dealCommunityCards();
        }, 1000);
    }, 1000);
}

function dealCommunityCards() {
    console.log("发公共牌...");
    let communityCards = [];
    
    // 发翻牌
    setTimeout(() => {
        dealer.currentAction = "发翻牌";
        for (let i = 0; i < 3; i++) {
            communityCards.push(deck.pop());
        }
        updateGameState({
            players: mockPlayers,
            communityCards: communityCards,
            pot: 0,
            dealer: dealer
        });
        
        // 发转牌
        setTimeout(() => {
            dealer.currentAction = "发转牌";
            communityCards.push(deck.pop());
            updateGameState({
                players: mockPlayers,
                communityCards: communityCards,
                pot: 0,
                dealer: dealer
            });
            
            // 发河牌
            setTimeout(() => {
                dealer.currentAction = "发河牌";
                communityCards.push(deck.pop());
                updateGameState({
                    players: mockPlayers,
                    communityCards: communityCards,
                    pot: 0,
                    dealer: dealer
                });
                
                dealer.isDealing = false;
                dealer.currentAction = "等待玩家行动";
            }, 1000);
        }, 1000);
    }, 1000);
}

function handleAction(action, data = {}) {
    console.log(`执行操作: ${action}`, data);
    // 模拟操作响应
    const response = {
        success: true,
        message: `${action} 操作成功`,
        gameState: {
            players: mockPlayers,
            communityCards: currentGame ? currentGame.communityCards : [],
            pot: currentGame ? currentGame.pot : 0,
            dealer: dealer
        }
    };
    
    updateGameState(response.gameState);
    showMessage(response.message);
}

function handleLeaveGame() {
    console.log("处理离开游戏...");
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    window.location.href = '/index.html';
}

function updateGameState(gameState) {
    console.log("更新游戏状态...", gameState);
    currentGame = gameState;
    
    // 更新公共牌
    updateCommunityCards(gameState.communityCards);
    
    // 更新玩家信息
    updatePlayers(gameState.players);
    
    // 更新奖池
    document.getElementById('potAmount').textContent = gameState.pot;
    
    // 更新荷官状态
    updateDealer(gameState.dealer);
}

function updateCommunityCards(cards) {
    console.log("更新公共牌...", cards);
    const container = document.getElementById('communityCards');
    container.innerHTML = '';
    cards.forEach(card => {
        const cardElement = document.createElement('div');
        cardElement.className = 'card';
        cardElement.textContent = card;
        container.appendChild(cardElement);
    });
}

function updatePlayers(players) {
    console.log("更新玩家信息...", players);
    const container = document.querySelector('.player-positions');
    container.innerHTML = '';
    players.forEach(player => {
        const playerElement = document.createElement('div');
        playerElement.className = 'player-seat' + 
            (player.active ? ' active' : '') + 
            (player.username === currentPlayer.username ? ' current-player' : '') +
            (player.username === currentActionPlayer?.username ? ' current-turn' : '');
        
        playerElement.innerHTML = `
            <div class="player-info">
                <div class="player-name">${player.username}</div>
                <div class="player-role">${player.role}</div>
                <div class="player-chips">${player.chips}</div>
                <div class="player-bet">${player.currentBet > 0 ? '下注: ' + player.currentBet : ''}</div>
            </div>
            <div class="player-cards">
                ${player.cards.map(card => `<div class="card">${card}</div>`).join('')}
            </div>
        `;
        container.appendChild(playerElement);
    });
}

function updateDealer(dealer) {
    console.log("更新荷官状态...", dealer);
    const dealerStatus = document.getElementById('dealerStatus');
    if (dealerStatus) {
        dealerStatus.textContent = dealer.currentAction;
    }
}

function showMessage(message) {
    console.log("显示消息:", message);
    const gameStatus = document.getElementById('gameStatus');
    if (gameStatus) {
        gameStatus.textContent = message;
        gameStatus.style.display = 'block';
        setTimeout(() => {
            gameStatus.style.display = 'none';
        }, 3000);
    }
}

function showError(message) {
    console.log("显示错误:", message);
    const modal = new bootstrap.Modal(document.getElementById('gameModal'));
    const modalBody = document.querySelector('.modal-body');
    if (modalBody) {
        modalBody.textContent = message;
        modal.show();
    }
}

// 获取大盲注玩家
function getBigBlindPlayer() {
    return mockPlayers.find(p => p.role === "大盲注(BB)");
}

// 获取下一个未弃牌的玩家
function getNextActivePlayer(currentPlayer) {
    const currentIndex = mockPlayers.findIndex(p => p.username === currentPlayer.username);
    let nextIndex = (currentIndex + 1) % mockPlayers.length;
    
    while (nextIndex !== currentIndex) {
        if (mockPlayers[nextIndex].active) {
            return mockPlayers[nextIndex];
        }
        nextIndex = (nextIndex + 1) % mockPlayers.length;
    }
    return currentPlayer;
}

// 轮换位置
function rotatePositions() {
    const roles = ["庄家(D)", "小盲注(SB)", "大盲注(BB)", "", ""];
    mockPlayers.forEach((player, index) => {
        const newRoleIndex = (index + 1) % roles.length;
        player.role = roles[newRoleIndex];
    });
}

// 处理模拟玩家操作
function handleSimulatedAction(playerName, action, data = {}) {
    console.log(`模拟玩家 ${playerName} 执行操作: ${action}`, data);
    
    const player = mockPlayers.find(p => p.username === playerName);
    if (!player) {
        showError('找不到指定的玩家');
        return;
    }

    if (player.username !== currentActionPlayer.username) {
        showError('现在不是该玩家的行动回合');
        return;
    }

    // 更新玩家状态
    switch (action) {
        case 'check':
            showMessage(`${playerName} 选择看牌`);
            break;
        case 'raise':
            const amount = data.amount || 50;
            player.chips -= amount;
            player.currentBet += amount;
            currentGame.pot += amount;
            showMessage(`${playerName} 加注 ${amount} 筹码`);
            break;
        case 'fold':
            player.active = false;
            showMessage(`${playerName} 选择弃牌`);
            break;
    }

    // 更新下一个行动玩家
    currentActionPlayer = getNextActivePlayer(player);
    dealer.currentAction = `等待 ${currentActionPlayer.username} 行动`;

    // 更新游戏状态
    updateGameState({
        players: mockPlayers,
        communityCards: currentGame ? currentGame.communityCards : [],
        pot: currentGame ? currentGame.pot + (data.amount || 0) : 0,
        dealer: dealer
    });
}