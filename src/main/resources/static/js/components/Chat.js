const Chat = ({ game, player, onSendMessage }) => {
    const [message, setMessage] = React.useState('');
    const [messages, setMessages] = React.useState([]);
    const [selectedPlayer, setSelectedPlayer] = React.useState(null);
    const chatContainerRef = React.useRef(null);

    React.useEffect(() => {
        if (chatContainerRef.current) {
            chatContainerRef.current.scrollTop = chatContainerRef.current.scrollHeight;
        }
    }, [messages]);

    const handleSendMessage = (e) => {
        e.preventDefault();
        if (message.trim()) {
            onSendMessage(message, selectedPlayer !== null, selectedPlayer);
            setMessage('');
            setSelectedPlayer(null);
        }
    };

    const handlePlayerSelect = (playerId) => {
        setSelectedPlayer(playerId === selectedPlayer ? null : playerId);
    };

    return (
        <div className="chat-container">
            <div className="chat-messages" ref={chatContainerRef}>
                {messages.map((msg, index) => (
                    <div key={index} className={`chat-message ${msg.isPrivate ? 'private' : ''}`}>
                        <span className="chat-sender">{msg.sender}:</span>
                        <span className="chat-content">{msg.content}</span>
                    </div>
                ))}
            </div>
            <form className="chat-input" onSubmit={handleSendMessage}>
                <select
                    value={selectedPlayer || ''}
                    onChange={(e) => handlePlayerSelect(e.target.value)}
                >
                    <option value="">所有人</option>
                    {Object.values(game.players)
                        .filter(p => p.id !== player.id)
                        .map(p => (
                            <option key={p.id} value={p.id}>
                                {p.name}
                            </option>
                        ))
                    }
                </select>
                <input
                    type="text"
                    value={message}
                    onChange={(e) => setMessage(e.target.value)}
                    placeholder={selectedPlayer ? "发送私人消息..." : "发送消息..."}
                />
                <button type="submit">发送</button>
            </form>
        </div>
    );
};