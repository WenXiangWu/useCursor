document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('loginForm');
    
    loginForm.addEventListener('submit', function(e) {
        e.preventDefault();
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        
        login(username, password);
    });
});

function login(username, password) {
    fetch('/poker/api/auth/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            username: username,
            password: password
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('登录失败');
        }
        return response.json();
    })
    .then(data => {
        localStorage.setItem('token', data.token);
        localStorage.setItem('username', username);
        window.location.href = '/poker/game.html';
    })
    .catch(error => {
        alert('登录失败：' + error.message);
    });
}

// WebSocket连接
let stompClient = null;

function connectWebSocket() {
    const socket = new SockJS('/poker/ws');
    stompClient = Stomp.over(socket);
    
    stompClient.connect({}, function(frame) {
        console.log('WebSocket连接成功');
        
        // 订阅游戏状态更新
        stompClient.subscribe('/topic/game/state', function(message) {
            updateGameState(JSON.parse(message.body));
        });
        
        // 订阅聊天消息
        stompClient.subscribe('/topic/chat', function(message) {
            addChatMessage(JSON.parse(message.body));
        });
        
        // 订阅错误消息
        stompClient.subscribe('/user/queue/errors', function(message) {
            handleError(JSON.parse(message.body));
        });
    }, function(error) {
        console.error('WebSocket连接失败:', error);
        setTimeout(connectWebSocket, 5000); // 5秒后重试
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
        console.log("WebSocket已断开");
    }
}

// 游戏状态更新处理
function updateGameState(gameState) {
    console.log('收到游戏状态更新:', gameState);
    // TODO: 实现游戏状态更新逻辑
}

// 聊天消息处理
function addChatMessage(message) {
    console.log('收到聊天消息:', message);
    // TODO: 实现聊天消息显示逻辑
}

// 错误处理
function handleError(error) {
    console.error('收到错误消息:', error);
    alert('发生错误：' + error.message);
} 