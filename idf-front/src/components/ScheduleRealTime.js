import React, { useEffect, useState } from 'react';
import axios from 'axios';
import '../styles/ScheduleRT.css';
import { ALLLIGNES, SCHEDULEREALTIME, SCHEDULEREALTIMEPERTUBATED } from '../constants/back';

export default function BusTable() {
    const [busLines, setBusLines] = useState([]);
    const [selectedLine, setSelectedLine] = useState('');
    const [realTimeSchedule, setRealTimeSchedule] = useState({});
    const [randomLineSchedules, setRandomLineSchedules] = useState({});

    useEffect(() => {
        const fetchBusLines = async () => {
            try {
                const response = await axios.get(ALLLIGNES);
                const lines = response.data;
                const randomLineSchedules = lines.reduce((acc, line) => {
                    acc[line] = Math.random() < 0.5 ? 'realTimeSchedule' : 'realTimeScheduleperturbated';
                    return acc;
                }, {});
                setRandomLineSchedules(randomLineSchedules);
                setBusLines(lines);
            } catch (error) {
                console.error('Error fetching data from the backend:', error);
            }
        };

        fetchBusLines();
    }, []);

    const updateLineData = async (line) => {
        try {
            setSelectedLine(line);
            const localTime = new Date().toLocaleTimeString('en-US', { hour12: false, hour: '2-digit', minute: '2-digit' });
            const scheduleType = randomLineSchedules[line] === 'realTimeSchedule' ? SCHEDULEREALTIME : SCHEDULEREALTIMEPERTUBATED;
            const response = await axios.get(`${scheduleType}/${localTime}/${line}`);
            setRealTimeSchedule(response.data);
        } catch (error) {
            console.error('Error fetching data from the backend:', error);
        }
    };

    const handleLineChange = (event) => {
        const line = event.target.value;
        updateLineData(line);
    };



    return (
        <div className="busSchedule1">
            <div className="container21">
                <h2>Choose the line:</h2>
                <select value={selectedLine} onChange={handleLineChange}>
                    <option value="">Select</option>
                    {busLines.map((line) => (
                        <option key={line} value={line}>{line}</option>
                    ))}
                </select>
            </div>
            <div className="realTime1">
                {Object.entries(randomLineSchedules).map(([line, scheduleType], key) => (
                    scheduleType === 'realTimeScheduleperturbated' && (
                        <p key={key}>The line {line} is perturbed</p>
                    )
                ))}
            </div>
            <div className="stationsHeader1">
                <h1>Station Name:</h1>
            </div>
            <div className="stations1">
                <div className="stations11">
                    <table>
                        <tbody>
                        {}
                        {Object.entries(realTimeSchedule).map(([busNumber, schedules]) => (
                            <React.Fragment key={busNumber}>
                                {/* Display the bus number */}
                                <tr>
                                    <td colSpan={2}><strong>Bus {busNumber}:</strong></td>
                                </tr>

                                {schedules.map((schedule, index) => (
                                    <tr key={index}>
                                        {Object.keys(schedule).map((station, innerIndex) => (
                                            <React.Fragment key={innerIndex}>
                                                <td>{station}</td>
                                                <td>{schedule[station]}</td>
                                            </React.Fragment>
                                        ))}
                                    </tr>
                                ))}
                            </React.Fragment>
                        ))}
                        </tbody>
                    </table>
                </div>
            </div>

        </div>
    );
}
