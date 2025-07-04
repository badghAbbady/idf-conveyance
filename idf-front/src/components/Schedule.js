import React, {useEffect, useRef, useState} from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../styles/Schedule.css';
import { FaBus } from "react-icons/fa";
import { FaTrainTram } from "react-icons/fa6";
import BusSchedule from "./BusSchedule";
import TramSchedule from "./TramSchedule";
import {BUSLIGNES} from "../constants/back";
import {TRAMLIGNES} from "../constants/back";
import axios from "axios";




export default function Schedule () {
    const busRef=useRef();
    const tramRef=useRef();

    const [activeLink, setActiveLink] = useState(null); // État pour suivre le lien actif

    const [activeDiv,setActiveDiv]= useState(null);

    const [busLignes,setBusLignes]=useState([]);

    const [tramLignes,setTramLignes]=useState([]);





    const handleNavClick = (ref) => {
        setActiveLink(ref.current.id);
        setActiveDiv(ref.current.id);
    }

    useEffect(() => {
        axios.get(BUSLIGNES)
            .then(response => setBusLignes(response.data))
            .catch(error => console.error('Error fetching data from the backend:', error));
    }, []);

    useEffect(() => {
        axios.get(TRAMLIGNES)
            .then(response => setTramLignes(response.data))
            .catch(error => console.error('Error fetching data from the backend:', error));
    }, []);

    const allLignes=[...busLignes,...tramLignes]




    return (
        <div className='Schedule'>
            <div className='realTime'>
                <p>Schedules</p>
            </div>
            <div className='Container1'>
                <nav className="navbar" id="miniNav">
                    <div className="container-fluid" >
                        <a  className={`navbar-brand ${activeLink === 'bus' ? 'active' : ''}`}
                            href="#"
                            ref={busRef}
                            id="bus"
                            onClick={() => handleNavClick(busRef)}>
                            <FaBus />
                                BUS
                        </a>
                        <a className={`navbar-brand ${activeLink === 'tram' ? 'active' : ''}`}
                           href="#"
                           ref={tramRef}
                           id="tram"
                           onClick={() => handleNavClick(tramRef)}>
                            <FaTrainTram />
                            TRAM
                        </a>
                    </div>
                </nav>
                {activeDiv==='bus' &&
                    <BusSchedule />
                }
                {activeDiv==='tram' &&
                    <TramSchedule/>
                }
            </div>
        </div>




    )

}