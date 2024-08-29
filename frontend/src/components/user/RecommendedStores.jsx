import React from 'react';

function RecommendedStores({ stores }) {
    return (
        <div style={{ maxWidth: '600px', margin: '20px auto', padding: '15px', backgroundColor: '#e0f7fa', borderRadius: '8px', boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)' }}>
            <h2 style={{ color: '#00796b', fontFamily: 'Arial, sans-serif', fontSize: '1.5rem', marginBottom: '15px', textAlign: 'center' }}>추천 가게</h2>
            <ul style={{ listStyleType: 'none', paddingLeft: '0' }}>
                {stores.map((store, index) => (
                    <li key={index} style={{ marginBottom: '12px', padding: '12px', border: '1px solid #00796b', borderRadius: '5px', backgroundColor: '#ffffff', boxShadow: '0 2px 4px rgba(0, 0, 0, 0.08)' }}>
                        <h3 style={{ margin: '0 0 8px 0', fontFamily: 'Verdana, sans-serif', fontSize: '1.2rem', color: '#004d40' }}>{store.storeName}</h3>
                        <p style={{ margin: '0 0 5px 0', fontFamily: 'Georgia, serif', fontSize: '0.9rem', color: '#555' }}>Address: {store.address}</p>
                        <p style={{ margin: '0 0 5px 0', fontFamily: 'Georgia, serif', fontSize: '0.9rem', color: '#555' }}>Score: {store.score.toFixed(2)}</p>
                        <p style={{ margin: '0', fontFamily: 'Georgia, serif', fontSize: '0.9rem', color: '#555' }}>Clustered Terms: {store.clusteredTerms}</p>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default RecommendedStores;