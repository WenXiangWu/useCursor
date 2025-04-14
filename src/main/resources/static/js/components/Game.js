const Game = ({ game, player, onPlayerAction, onLeaveGame }) => {
    const [raiseAmount, setRaiseAmount] = React.useState(0);

    const handleFold = () => {
        onPlayerAction('fold');
    };

    const handleCall = () => {
        onPlayerAction('call');
    };

    const handleRaise = () => {
        if (raiseAmount > 0) {
            onPlayerAction('raise', raiseAmount);
        }
    };

    const handleLeave = () => {
        onLeaveGame();
    };

    return (
        <div className="game-table">
            <div className="game-info">
                <div className="blinds-info">
                    <span>小盲: {game.config.smallBlind}</span>
                    <span>大盲: {game.config.bigBlind}</span>
                </div>
                <div className="round-info">
                    <span>轮次: {game.currentRound}</span>
                </div>
            </div>

            <div className="community-cards">
                {game.communityCards.map((card, index) => (
                    <Card key={index} card={card} />
                ))}
            </div>

            <div className="pot">
                奖池: {game.pot}
            </div>

            <div className="player-seats">
                {Object.values(game.players).map((seatPlayer) => (
                    <Player
                        key={seatPlayer.id}
                        player={seatPlayer}
                        isCurrentPlayer={seatPlayer.id === game.currentPlayerId}
                        isDealer={seatPlayer.id === game.dealerId}
                        isSmallBlind={seatPlayer.isSmallBlind}
                        isBigBlind={seatPlayer.isBigBlind}
                        totalBet={game.playerBets[seatPlayer.id] || 0}
                    />
                ))}
            </div>

            {player && player.id === game.currentPlayerId && (
                <div className="player-actions">
                    <button 
                        className="action-button"
                        onClick={handleFold}
                        disabled={player.folded}
                    >
                        弃牌
                    </button>
                    <button 
                        className="action-button"
                        onClick={handleCall}
                        disabled={player.folded || player.isAllIn}
                    >
                        跟注
                    </button>
                    <div className="raise-controls">
                        <input
                            type="number"
                            value={raiseAmount}
                            onChange={(e) => setRaiseAmount(parseInt(e.target.value) || 0)}
                            min={game.config.bigBlind}
                            max={player.chips}
                        />
                        <button 
                            className="action-button"
                            onClick={handleRaise}
                            disabled={player.folded || player.isAllIn || raiseAmount <= 0}
                        >
                            加注
                        </button>
                    </div>
                    <button 
                        className="action-button"
                        onClick={handleLeave}
                    >
                        离开游戏
                    </button>
                </div>
            )}
        </div>
    );
}; 