import React from 'react';

function StoreList({ stores }) {
    return (
        <div style={{ maxWidth: '400px', margin: '20px auto', padding: '20px', backgroundColor: '#f9f9f9', borderRadius: '10px', boxShadow: '0 2px 10px rgba(0, 0, 0, 0.1)' }}>
            <h2 style={{ color: '#333', fontFamily: 'Arial, sans-serif', marginBottom: '20px', textAlign: 'center' }}>추천 가게</h2>
            <ul style={{ listStyleType: 'none', paddingLeft: '0' }}>
                {stores.map((store, index) => (
                    <li key={index} style={{ marginBottom: '15px', padding: '15px', border: '1px solid #ddd', borderRadius: '5px', backgroundColor: '#fff', boxShadow: '0 1px 3px rgba(0, 0, 0, 0.1)' }}>
                        <h3 style={{ margin: '0 0 10px 0', fontFamily: 'Verdana, sans-serif', color: '#444' }}>{store.storeName}</h3>
                        <p style={{ margin: '0', fontFamily: 'Georgia, serif', color: '#666' }}>Address: {store.address}</p>
                        <p style={{ margin: '5px 0 0 0', fontFamily: 'Georgia, serif', color: '#666' }}>Score: {store.score.toFixed(2)}</p>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default StoreList;
