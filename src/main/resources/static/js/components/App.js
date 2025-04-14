const App = () => {
    const [game, setGame] = React.useState(null);
    const [player, setPlayer] = React.useState(null);
    const [stompClient, setStompClient] = React.useState(null);
    const [connected, setConnected] = React.useState(false);

    React.useEffect(() => {
        const socket = new SockJS('/ws');
        const client = Stomp.over(socket);
        
        client.connect({}, () => {
            setConnected(true);
            setStompClient(client);
            
            // 订阅游戏事件
            client.subscribe('/topic/games', (message) => {
                const gameData = JSON.parse(message.body);
                setGame(gameData);
            });
            
            client.subscribe('/user/queue/chat', (message) => {
                const chatData = JSON.parse(message.body);
                // 处理私人聊天消息
            });
        }, (error) => {
            console.error('WebSocket连接错误:', error);
        });

        return () => {
            if (client) {
                client.disconnect();
            }
        };
    }, []);

    const createGame = (config) => {
        if (stompClient && connected) {
            stompClient.send("/app/game.create", {}, JSON.stringify(config));
        }
    };

    const joinGame = (gameId, playerName, chips) => {
        if (stompClient && connected) {
            const playerData = {
                gameId: gameId,
                name: playerName,
                chips: chips
            };
            stompClient.send("/app/game.join", {}, JSON.stringify(playerData));
        }
    };

    const leaveGame = () => {
        if (stompClient && connected && player) {
            stompClient.send("/app/game.leave", {}, JSON.stringify({
                gameId: game.id,
                playerId: player.id
            }));
            setGame(null);
            setPlayer(null);
        }
    };

    const playerAction = (action, amount = 0) => {
        if (stompClient && connected && game && player) {
            stompClient.send("/app/game.action", {}, JSON.stringify({
                gameId: game.id,
                playerId: player.id,
                action: action,
                amount: amount
            }));
        }
    };

    const sendChatMessage = (message, isPrivate = false, targetPlayerId = null) => {
        if (stompClient && connected && game && player) {
            stompClient.send("/app/game.chat", {}, JSON.stringify({
                gameId: game.id,
                playerId: player.id,
                message: message,
                isPrivate: isPrivate,
                targetPlayerId: targetPlayerId
            }));
        }
    };

    if (!connected) {
        return <div className="loading">正在连接服务器...</div>;
    }

    if (!game) {
        return (
            <div className="game-container">
                <h1>德州扑克游戏</h1>
                <div className="game-creation">
                    <h2>创建新游戏</h2>
                    <button onClick={() => createGame({
                        minPlayers: 2,
                        maxPlayers: 9,
                        startingChips: 1000,
                        smallBlind: 5,
                        bigBlind: 10
                    })}>创建游戏</button>
                </div>
                <div className="game-list">
                    <h2>可用游戏</h2>
                    {/* 游戏列表将在这里显示 */}
                </div>
            </div>
        );
    }

    return (
        <div className="game-container">
            <Game 
                game={game}
                player={player}
                onPlayerAction={playerAction}
                onLeaveGame={leaveGame}
            />
            <Chat 
                game={game}
                player={player}
                onSendMessage={sendChatMessage}
            />
        </div>
    );
}; 