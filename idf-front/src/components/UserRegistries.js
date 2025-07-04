import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../styles/userRegistries.css';
import {ALLREGISTRIES, REGISTRIESBYSUBSCRIBERS} from "../constants/back";

const UserRegistries = () => {
    const [registries, setRegistries] = useState([]);
    const [searchUsername, setSearchUsername] = useState('');
    const searchParams = new URLSearchParams(window.location.search);
    const usernameParam = searchParams.get('username');

    //fetching registries based on who logged in
    useEffect(() => {
        if (usernameParam && usernameParam.toLowerCase() === 'admin') { //the admin has full access to all the regitries
            axios.get(ALLREGISTRIES)
                .then(response => {
                    const registriesData = response.data;
                    setRegistries(registriesData);
                })
                .catch(error => {
                    console.error('Error fetching registries:', error);
                });
        } else {
            axios.get(`http://localhost:8080/subscribers/${usernameParam}/registries`)
                .then(response => {
                    const registriesData = response.data;
                    setRegistries(registriesData);
                    console.log(registriesData)
                })
                .catch(error => {
                    console.error('Error fetching registries:', error);
                });
        }
    }, [usernameParam]);

    //getting registries by the username the admin searched
    const handleSearch = () => {
        axios.get(`http://localhost:8080/subscribers/${searchUsername}/registries`)
            .then(response => {
                const registriesData = response.data;
                setRegistries(registriesData);
            })
            .catch(error => {
                console.error('Error fetching registries:', error);
            });
    };

    return (
        <div className="centered-container">
            <h1>Bonjour {usernameParam}! </h1>
            <h2>Voici vos enregistrements  </h2>

            {usernameParam && usernameParam.toLowerCase() === 'admin' && (
                <div className="search-bar">
                    <input
                        type="text"
                        placeholder="Rechercher..."
                        className="search-input"
                        value={searchUsername}
                        onChange={(e) => setSearchUsername(e.target.value)}
                    />
                    <button className="search-button" onClick={handleSearch}>Search</button>
                </div>
            )}

            <table>
                <thead>
                <tr>
                    <th>ID of Registry</th>
                    <th>Date of Registry</th>
                    <th>Entry Station</th>
                    <th>Exit Station</th>
                    <th>Transportation Mean</th>
                </tr>
                </thead>
                <tbody>
                {registries.map(registry => (
                    <tr key={registry.idHistory}>
                        <td>{registry.idHistory}</td>
                        <td>{registry.dateRegistry}</td>
                        <td>{registry.entryStation}</td>
                        <td>{registry.exitStation}</td>
                        {/* Display the ligneCode */}
                        <td>{registry.transportationMeanH ? "Ligne "+registry.transportationMeanH.ligneCode +", Bus " +registry.transportationMeanH.transportationMeanId : 'N/A'}</td>
                    </tr>
                ))}

                {console.log(registries)}
                </tbody>
            </table>
        </div>
    );
};

export default UserRegistries;
