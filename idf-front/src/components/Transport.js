import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../styles/Transport.css';
import Popup from './Popup';

function Transport() {
    const [transportationMeanId, setTransportationMeanId] = useState('');
    const [lines, setLines] = useState([]);
    const [result, setResult] = useState('');
    const [showPopup, setShowPopup] = useState(false);
    const localTime = new Date().toLocaleTimeString('en-US', { hour12: false, hour: '2-digit', minute:'2-digit'})
    useEffect(() => {
        const fetchTransportationLines = async () => {
            try {
                const response = await axios.get('http://localhost:8080/transportationMean/all');
                setLines(response.data);
            } catch (error) {
                console.error('Error fetching transportation lines:', error);
            }
        };

        fetchTransportationLines();
    }, []);



    const handleTransportationMeanIdChange = (e) => {
        setTransportationMeanId(e.target.value);
    };

    const handleFetchSubscribers = async () => {
        try {
            if (!transportationMeanId) {
                setResult('Please select a transportation line.');
                setShowPopup(true);
                return;
            }

            const response = await axios.get(`http://localhost:8080/registry/counter/${transportationMeanId}`);
            const count = response.data;
            console.log(count)
            setResult(`Number of Subscribers in the selected line: ${count}`);
            setShowPopup(true);
        } catch (error) {
            setResult(`Error: ${error.message}`);
            setShowPopup(true);
        }
    };

    const closePopup = () => {
        setShowPopup(false);
    };

    return (
        <div className="Transport">
            <div className='Container'>
                <h1>Transportation Mean Subscriber Count</h1>
                <div>
                    <label>
                        Select Transportation Line:
                        <select
                            name="transportationMeanId"
                            value={transportationMeanId}
                            onChange={handleTransportationMeanIdChange}
                            style={{ color: 'black' }}
                        >
                            <option value="">Select a Line</option>
                            {lines.map((line) => (
                                <option key={line.transportationMeanId} value={line.transportationMeanId} >
                                    {line.transportationMeanId}
                                </option>
                            ))}
                        </select>
                    </label>
                </div>
                <button id='button' onClick={handleFetchSubscribers} >Subscriber Count</button>
            </div>

            <div className='Result'>
                <p>{result}</p>
            </div>

            {showPopup && <Popup message={result} onClose={closePopup} />}
        </div>
    );
}

export default Transport;
