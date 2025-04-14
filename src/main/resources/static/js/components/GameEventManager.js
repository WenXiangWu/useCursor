class GameEventManager {
    constructor() {
        this.listeners = new Map();
        this.stompClient = null;
    }

    connect() {
        const socket = new SockJS('/ws');
        this.stompClient = Stomp.over(socket);
        
        this.stompClient.connect({}, () => {
            console.log('WebSocket连接成功');
            this.subscribeToEvents();
        }, (error) => {
            console.error('WebSocket连接错误:', error);
        });
    }

    subscribeToEvents() {
        // 订阅游戏事件
        this.stompClient.subscribe('/topic/games', (message) => {
            const gameData = JSON.parse(message.body);
            this.notifyListeners('gameUpdate', gameData);
        });

        // 订阅特定游戏的事件
        this.stompClient.subscribe('/topic/game.*', (message) => {
            const eventData = JSON.parse(message.body);
            this.handleGameEvent(eventData);
        });

        // 订阅聊天消息
        this.stompClient.subscribe('/topic/game.*.chat', (message) => {
            const chatData = JSON.parse(message.body);
            this.notifyListeners('chatMessage', chatData);
        });

        // 订阅私人消息
        this.stompClient.subscribe('/user/queue/chat', (message) => {
            const privateMessage = JSON.parse(message.body);
            this.notifyListeners('privateMessage', privateMessage);
        });
    }

    handleGameEvent(eventData) {
        switch (eventData.type) {
            case 'GAME_STARTED':
                this.notifyListeners('gameStarted', eventData);
                break;
            case 'PLAYER_JOINED':
                this.notifyListeners('playerJoined', eventData);
                break;
            case 'PLAYER_LEFT':
                this.notifyListeners('playerLeft', eventData);
                break;
            case 'PLAYER_ACTION':
                this.notifyListeners('playerAction', eventData);
                break;
            case 'BLINDS_CHANGED':
                this.notifyListeners('blindsChanged', eventData);
                break;
            case 'GAME_PAUSED':
                this.notifyListeners('gamePaused', eventData);
                break;
            case 'GAME_RESUMED':
                this.notifyListeners('gameResumed', eventData);
                break;
            case 'HAND_STARTED':
                this.notifyListeners('handStarted', eventData);
                break;
            case 'CARDS_DEALT':
                this.notifyListeners('cardsDealt', eventData);
                break;
            case 'COMMUNITY_CARDS_DEALT':
                this.notifyListeners('communityCardsDealt', eventData);
                break;
            case 'ROUND_ENDED':
                this.notifyListeners('roundEnded', eventData);
                break;
            case 'PLAYER_ELIMINATED':
                this.notifyListeners('playerEliminated', eventData);
                break;
            case 'GAME_ENDED':
                this.notifyListeners('gameEnded', eventData);
                break;
        }
    }

    addEventListener(eventType, callback) {
        if (!this.listeners.has(eventType)) {
            this.listeners.set(eventType, new Set());
        }
        this.listeners.get(eventType).add(callback);
    }

    removeEventListener(eventType, callback) {
        if (this.listeners.has(eventType)) {
            this.listeners.get(eventType).delete(callback);
        }
    }

    notifyListeners(eventType, data) {
        if (this.listeners.has(eventType)) {
            this.listeners.get(eventType).forEach(callback => {
                try {
                    callback(data);
                } catch (error) {
                    console.error(`处理事件 ${eventType} 时出错:`, error);
                }
            });
        }
    }

    disconnect() {
        if (this.stompClient) {
            this.stompClient.disconnect();
            this.stompClient = null;
        }
    }
}

// 导出单例实例
const gameEventManager = new GameEventManager();
export default gameEventManager;