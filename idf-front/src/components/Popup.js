import React from 'react';
import '../styles/popup.css';

const Popup = ({ message, onClose }) => {
    return (
        <div className="popup">
            <div className="popup-content">
                <p>{message}</p>
                <button onClick={onClose}>OK</button>
            </div>
        </div>
    );
};

export default Popup;
