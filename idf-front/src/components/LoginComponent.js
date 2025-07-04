import React, { useState } from 'react';
import axios from 'axios';
import '../styles/style.css';
import './UserInfo'
import './UserRegistries'
import {AUTHENTIFICATIONBYLOGIN, AUTHENTIFICATIONBYSIGNUP} from "../constants/back";

const LoginComponent = () => {
    // Declaring state variables using useState hook
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [newUser, setNewUser] = useState({
        username: '',
        password: ''
    });

    const [isSignUp, setIsSignUp] = useState(false); // isSignUp state variable for tracking sign up mode


    const handleLogin = () => {
        axios.post( AUTHENTIFICATIONBYLOGIN, { username, password })
            .then(response => {
                window.location.href = `/user-registries?username=${encodeURIComponent(username)}`; // Redirecting to user-registries page with username parameter
            })
            .catch(error => {
                console.error('Authentication failed:', error);
                alert('Username or password incorrect.');
            });
    };





    const handleSignup = () => {
        axios.post( AUTHENTIFICATIONBYSIGNUP, newUser)
            .then(response => {
                window.location.href = `/user-info?username=${encodeURIComponent(newUser.username)}`;// Redirecting to user-info page with username parameter

            })
            .catch(error => {
                console.error('Registration failed:', error);
            });
    };

    // Function to switch between sign up and sign in by toggle
    const toggleForm = () => {
        const container = document.getElementById('container');//the main container
        const registerBtn = document.getElementById('register');
        const loginBtn = document.getElementById('login');

        registerBtn.addEventListener('click', () => {
            container.classList.add("active"); // when registerBtn is clicked the register form is activated and login form is hidden
        });

        loginBtn.addEventListener('click', () => {
            container.classList.remove("active"); // when registerBtn is clicked the register form is removed and login form is activated
        });
    };


    return (
        <div className="LoginComponent container" id="container">
            <div className="form-container sign-up active">
                <form>
                    <h1>Create Account</h1>
                    <input type="text" placeholder="Username" value={newUser.username} onChange={(e) => setNewUser({ ...newUser, username: e.target.value })} />
                    <input type="password" placeholder="Password" value={newUser.password} onChange={(e) => setNewUser({ ...newUser, password: e.target.value })} />
                    <button type="button" onClick={handleSignup} >Sign Up</button>
                </form>
            </div>
            <div className="form-container sign-in">
                <form>
                    <h1>Sign In</h1>
                    <div className="social-icons">
                        <a href="#" className="icon"><i className="fa-brands fa-google-plus-g"></i></a>
                        <a href="#" className="icon"><i className="fa-brands fa-facebook-f"></i></a>
                        <a href="#" className="icon"><i className="fa-brands fa-github"></i></a>
                        <a href="#" className="icon"><i className="fa-brands fa-linkedin-in"></i></a>
                    </div>
                    <span>or use your username and password</span>
                    <input type="username" placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)} />
                    <input type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} />
                    <a href="#">Forget Your Password?</a>
                    <button type="button" onClick={handleLogin}>Sign In</button>
                </form>
            </div>
            <div className="toggle-container">
                <div className={'toggle ' + (isSignUp ? 'right-panel-active' : 'left-panel-active')}>
                    <div className="toggle-panel toggle-left">
                        <h2>Welcome Back!</h2>
                        <button onClick={toggleForm}  id="login">Sign In</button>
                    </div>
                    <div className="toggle-panel toggle-right">
                        <h2>Hello dear traveler!</h2>
                        <button onClick={toggleForm}  id="register">Sign Up</button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default LoginComponent;