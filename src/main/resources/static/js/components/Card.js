const Card = ({ card, hidden = false }) => {
    if (hidden) {
        return <div className="card hidden">🂠</div>;
    }

    const getSuitSymbol = (suit) => {
        switch (suit) {
            case 'HEARTS': return '♥';
            case 'DIAMONDS': return '♦';
            case 'CLUBS': return '♣';
            case 'SPADES': return '♠';
            default: return '';
        }
    };

    const getRankSymbol = (rank) => {
        switch (rank) {
            case 'ACE': return 'A';
            case 'KING': return 'K';
            case 'QUEEN': return 'Q';
            case 'JACK': return 'J';
            default: return rank;
        }
    };

    const isRed = card.suit === 'HEARTS' || card.suit === 'DIAMONDS';

    return (
        <div className={`card ${isRed ? 'red' : ''}`}>
            <div className="card-rank">{getRankSymbol(card.rank)}</div>
            <div className="card-suit">{getSuitSymbol(card.suit)}</div>
        </div>
    );
}; 