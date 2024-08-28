// src/components/StoreList.jsx

import React from 'react';

function StoreList({ stores }) {
    return (
        <div>
            <h2>여기는 추천</h2>
            <ul style={{ listStyleType: 'none', paddingRight:100 }}>
                {stores.map((store, index) => (
                    <li key={index} style={{ marginBottom: '5px', padding: '5px', border: '1px solid #ddd' }}>
                        <h3>{store.storeName}</h3>
                        <p>Address: {store.address}</p>
                        <p>Score: {store.score.toFixed(2)}</p>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default StoreList;
