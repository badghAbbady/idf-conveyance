import React, { useState, useEffect } from 'react';
import axios from 'axios';

const TicketDisplay = ({ match }) => {
    const ticketId = match.params.ticketId;
    const [ticketDetails, setTicketDetails] = useState(null);

    useEffect(() => {
        // Fetch ticket details using the ticketId from the backend
        const fetchTicketDetails = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/ticket/${ticketId}`);
                setTicketDetails(response.data);
            } catch (error) {
                console.error('Error fetching ticket details:', error);
            }
        };

        fetchTicketDetails();
    }, [ticketId]);

    return (
        <div>
            <h2>Your Ticket</h2>
            {ticketDetails ? (
                <div>
                    <p>Ticket ID: {ticketDetails.ticketId}</p>
                    <p>Visitor Name: {ticketDetails.visitor.firstName} {ticketDetails.visitor.lastName}</p>
                    <p>Email: {ticketDetails.visitor.email}</p>
                    <p>Serial number: {ticketDetails.serialNumber} </p>
                </div>
            ) : (
                <p>Loading ticket details...</p>
            )}
        </div>
    );
};

export default TicketDisplay;
