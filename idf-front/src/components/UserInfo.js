import React, { useEffect, useState } from 'react';
import { useLocation } from 'react-router-dom';
import '../styles/userInfo.css';

const UserInfo = () => {
    const location = useLocation();// useLocation hook allows access to information about the current URL
    const [username, setUsername] = useState('');

    useEffect(() => {
        const searchParams = new URLSearchParams(location.search);// Getting search parameters from URL
        const usernameParam = searchParams.get('username');// Getting 'username' parameter from search parameters
        setUsername(usernameParam || '');
    }, [location.search]);// Running the effect only when location.search changes


    //going back to login page function
    const handleBackToLogin = () => {
        window.location.href ='/login'// Redirecting to '/login' page
    };

    return (
        <div className="centered-container">
            <h1>Le groupe IDF-conveyance est heureux de vous retrouver!</h1>
            {username && (
                <h2>Bienvenue, {username} !</h2>
            )}
            {!username && (
                <p>Aucun nom d'utilisateur trouvé.</p>
            )}
            <div className="welcome-image-container">
                <img src="https://img.freepik.com/photos-premium/groupe-multiethnique-personnes-detenant-mot-francais-bienvenue-bienvenue_770123-11079.jpg" alt="Bienvenue" className="welcome-image" />
            </div>
            <button className="back-to-login-button" onClick={handleBackToLogin}>Let's go back to login :)</button>
        </div>
    );
};

export default UserInfo;
