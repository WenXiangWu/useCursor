const Player = ({ player, isCurrentPlayer, isDealer, isSmallBlind, isBigBlind, totalBet }) => {
    return (
        <div className={`player-seat ${isCurrentPlayer ? 'active' : ''} ${isDealer ? 'dealer' : ''}`}>
            <div className="player-info">
                <div className="player-name">{player.name}</div>
                <div className="player-stats">
                    <span className="player-chips">筹码: {player.chips}</span>
                    {totalBet > 0 && (
                        <span className="player-bet">下注: {totalBet}</span>
                    )}
                </div>
                {isSmallBlind && <div className="player-position">小盲</div>}
                {isBigBlind && <div className="player-position">大盲</div>}
            </div>
            <div className="player-cards">
                {player.hand ? (
                    player.hand.cards.map((card, index) => (
                        <Card 
                            key={index} 
                            card={card} 
                            hidden={!isCurrentPlayer && !player.folded}
                        />
                    ))
                ) : (
                    <div className="empty-hand">等待发牌</div>
                )}
            </div>
            {player.folded && (
                <div className="player-status folded">已弃牌</div>
            )}
            {player.isAllIn && (
                <div className="player-status all-in">全押</div>
            )}
        </div>
    );
};