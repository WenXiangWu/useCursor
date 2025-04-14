import gameEventManager from './GameEventManager.js';

class GameTable {
    constructor(gameId, playerId) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.socket = null;
        this.gameState = null;
        this.players = new Map();
        this.communityCards = [];
        this.playerCards = [];
    }

    init() {
        this.connectWebSocket();
        this.setupEventListeners();
        this.loadInitialState();
    }

    connectWebSocket() {
        this.socket = new WebSocket(`ws://${window.location.host}/ws/game/${this.gameId}`);
        
        this.socket.onopen = () => {
            console.log('WebSocket连接已建立');
        };
        
        this.socket.onmessage = (event) => {
            const message = JSON.parse(event.data);
            this.handleWebSocketMessage(message);
        };
        
        this.socket.onclose = () => {
            console.log('WebSocket连接已关闭');
        };
        
        this.socket.onerror = (error) => {
            console.error('WebSocket错误:', error);
        };
    }

    setupEventListeners() {
        document.getElementById('chatInput').addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                this.sendChatMessage();
            }
        });
    }

    loadInitialState() {
        fetch(`/api/games/${this.gameId}`)
            .then(response => response.json())
            .then(data => {
                this.updateGameState(data);
            })
            .catch(error => {
                console.error('加载游戏状态失败:', error);
            });
    }

    handleWebSocketMessage(message) {
        switch (message.type) {
            case 'GAME_STATE':
                this.updateGameState(message.data);
                break;
            case 'PLAYER_JOINED':
                this.addPlayer(message.data);
                break;
            case 'PLAYER_LEFT':
                this.removePlayer(message.data.playerId);
                break;
            case 'CARDS_DEALT':
                this.updateCards(message.data);
                break;
            case 'CHAT_MESSAGE':
                this.addChatMessage(message.data);
                break;
            default:
                console.log('未知消息类型:', message.type);
        }
    }

    updateGameState(state) {
        this.gameState = state;
        document.getElementById('gameState').textContent = this.getGameStateText();
        this.updatePlayerList();
        this.updateCommunityCards();
        this.updatePlayerCards();
    }

    getGameStateText() {
        if (!this.gameState) return '等待游戏开始...';
        
        switch (this.gameState.status) {
            case 'WAITING':
                return '等待玩家加入...';
            case 'STARTING':
                return '游戏即将开始...';
            case 'PLAYING':
                return `当前回合: ${this.gameState.currentRound}`;
            case 'ENDED':
                return '游戏已结束';
            default:
                return '未知状态';
        }
    }

    updatePlayerList() {
        const playerList = document.getElementById('playerList');
        playerList.innerHTML = '';
        
        this.gameState.players.forEach(player => {
            const playerElement = document.createElement('div');
            playerElement.className = `player ${player.id === this.playerId ? 'current-player' : ''}`;
            playerElement.innerHTML = `
                <div class="player-name">${player.name}</div>
                <div class="player-chips">${player.chips}</div>
                <div class="player-status">${this.getPlayerStatus(player)}</div>
            `;
            playerList.appendChild(playerElement);
        });
    }

    getPlayerStatus(player) {
        if (player.folded) return '已弃牌';
        if (player.allIn) return '全押';
        if (player.currentBet > 0) return `下注: ${player.currentBet}`;
        return '等待中';
    }

    updateCommunityCards() {
        const communityCards = document.getElementById('communityCards');
        communityCards.innerHTML = '';
        
        this.gameState.communityCards.forEach(card => {
            const cardElement = document.createElement('div');
            cardElement.className = 'card';
            cardElement.innerHTML = `${card.rank}${card.suit}`;
            communityCards.appendChild(cardElement);
        });
    }

    updatePlayerCards() {
        const playerCards = document.getElementById('playerCards');
        playerCards.innerHTML = '';
        
        const currentPlayer = this.gameState.players.find(p => p.id === this.playerId);
        if (currentPlayer && currentPlayer.cards) {
            currentPlayer.cards.forEach(card => {
                const cardElement = document.createElement('div');
                cardElement.className = 'card';
                cardElement.innerHTML = `${card.rank}${card.suit}`;
                playerCards.appendChild(cardElement);
            });
        }
    }

    addChatMessage(message) {
        const chatMessages = document.getElementById('chatMessages');
        const messageElement = document.createElement('div');
        messageElement.className = 'chat-message';
        messageElement.innerHTML = `
            <span class="chat-sender">${message.sender}:</span>
            <span class="chat-content">${message.content}</span>
        `;
        chatMessages.appendChild(messageElement);
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }

    sendChatMessage() {
        const input = document.getElementById('chatInput');
        const content = input.value.trim();
        
        if (content) {
            this.socket.send(JSON.stringify({
                type: 'CHAT_MESSAGE',
                data: {
                    gameId: this.gameId,
                    senderId: this.playerId,
                    content: content
                }
            }));
            input.value = '';
        }
    }

    addPlayer(player) {
        this.players.set(player.id, player);
        this.updatePlayerList();
    }

    removePlayer(playerId) {
        this.players.delete(playerId);
        this.updatePlayerList();
    }

    updateCards(data) {
        if (data.playerId === this.playerId) {
            this.playerCards = data.cards;
            this.updatePlayerCards();
        }
        if (data.communityCards) {
            this.communityCards = data.communityCards;
            this.updateCommunityCards();
        }
    }
}

export default GameTable; 