import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import Modal from 'react-modal';
import '../styles/TicketForm.css';

Modal.setAppElement('#root');

const TicketForm = () => {
    const navigate = useNavigate();

    const [visitorDetails, setVisitorDetails] = useState({
        firstName: '',
        lastName: '',
        email: '',
    });

    const [creditCardDetails, setCreditCardDetails] = useState({
        cardNumber: '',
        expirationDate: '',
        cvv: '',
    });

    const [modalIsOpen, setModalIsOpen] = useState(false);
    const [ticketId, setTicketId] = useState(null);
    const [purchaseTime, setPurchaseTime] = useState(null);
    const [ticketDetails, setTicketDetails] = useState(null);

    const handleVisitorChange = (e) => {
        setVisitorDetails({
            ...visitorDetails,
            [e.target.name]: e.target.value,
        });
    };

    const handleCreditCardChange = (e) => {
        setCreditCardDetails({
            ...creditCardDetails,
            [e.target.name]: e.target.value,
        });
    };

    const handlePurchase = async () => {
        if (!visitorDetails.firstName || !visitorDetails.lastName || !visitorDetails.email) {
            alert('Please fill in all visitor details.');
            return;
        }

        const isCardValid = await validateCreditCard(creditCardDetails);

        if (!isCardValid) {
            alert('Invalid credit card information.');
            return;
        }

        const existingVisitor = await findVisitorByEmail(visitorDetails.email);

        const visitor = existingVisitor || visitorDetails;

        const serialNumber = generateSerialNumber();

        const ticket = {
            visitor,
            serialNumber,
        };

        try {
            const response = await axios.post('http://localhost:8080/ticket/purchase', ticket);
            console.log('Ticket purchased:', response.data);

            setVisitorDetails({
                firstName: '',
                lastName: '',
                email: '',
            });

            setCreditCardDetails({
                cardNumber: '',
                expirationDate: '',
                cvv: '',
            });

            setTicketId(response.data.ticketId);
            const purchaseTime = new Date(response.data.purchaseTime);
            setPurchaseTime(purchaseTime);

            // Fetch ticket details by ID
            const ticketDetailsResponse = await axios.get(`http://localhost:8080/ticket/${response.data.ticketId}`);
            setTicketDetails(ticketDetailsResponse.data);

            setModalIsOpen(true);
        } catch (error) {
            console.error('Error purchasing ticket:', error);
        }
    };

    const validateCreditCard = async (creditCard) => {
        return true; // Simulate credit card validation
    };

    const findVisitorByEmail = async (email) => {
        try {
            const response = await axios.get(`http://localhost:8080/visitor/byEmail?email=${email}`);
            return response.data;
        } catch (error) {
            console.error('Error finding visitor by email:', error);
            return null;
        }
    };

    const generateSerialNumber = () => {
        const letters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
        const numbers = '0123456789';
        let serialNumber = '';

        for (let i = 0; i < 4; i++) {
            serialNumber += letters[Math.floor(Math.random() * letters.length)];
        }

        for (let i = 0; i < 6; i++) {
            serialNumber += numbers[Math.floor(Math.random() * numbers.length)];
        }

        return serialNumber;
    };

    const closeModal = () => {
        setModalIsOpen(false);
        navigate(`/ticket/${ticketId}`);
    };
    return (
        <div className="ticket-form-container">
            <h2>Buy Your Ticket</h2>
            <form className="ticket-form">
                {/* Visitor Details */}
                <div className="form-group">
                    <label htmlFor="firstName">First Name</label>
                    <input
                        type="text"
                        id="firstName"
                        name="firstName"
                        value={visitorDetails.firstName}
                        onChange={handleVisitorChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="lastName">Last Name</label>
                    <input
                        type="text"
                        id="lastName"
                        name="lastName"
                        value={visitorDetails.lastName}
                        onChange={handleVisitorChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="email">Email</label>
                    <input
                        type="email"
                        id="email"
                        name="email"
                        value={visitorDetails.email}
                        onChange={handleVisitorChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="cardNumber">Card Number</label>
                    <input
                        type="text"
                        id="cardNumber"
                        name="cardNumber"
                        value={creditCardDetails.cardNumber}
                        onChange={handleCreditCardChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="expirationDate">Expiration Date</label>
                    <input
                        type="text"
                        id="expirationDate"
                        name="expirationDate"
                        placeholder="MM/YYYY"
                        value={creditCardDetails.expirationDate}
                        onChange={handleCreditCardChange}
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="cvv">CVV</label>
                    <input
                        type="text"
                        id="cvv"
                        name="cvv"
                        value={creditCardDetails.cvv}
                        onChange={handleCreditCardChange}
                        required
                    />
                </div>

                <button type="button" onClick={handlePurchase}>
                    Purchase Ticket
                </button>
            </form>
            <Modal
                isOpen={modalIsOpen}
                onRequestClose={closeModal}
                contentLabel="Ticket Details Modal"
            >
                <h2>Ticket Purchased Successfully!</h2>
                <p>Ticket ID: {ticketId}</p>
                {ticketDetails && (
                    <>
                        <p>Purchase Time: {ticketDetails.purchaseHour}</p>
                        {/* Add other ticket details here */}
                    </>
                )}
                <button onClick={closeModal}>Close</button>
            </Modal>
        </div>
    );
};

export default TicketForm;
