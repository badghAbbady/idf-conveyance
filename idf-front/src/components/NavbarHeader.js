import React from 'react';
import {Link} from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css';
import logo from '../assets/Capture_d_écran_2023-11-22_à_15.04.44-removebg-preview.png';

import homeIcon from '../assets/img_6.png'
import mapIcon from '../assets/img_5.png'
import horaire from '../assets/img_4.png'
import '../styles/Navbar.css';




export default function NavbarHeader() {
    return (
        <nav className='nav'>
            <div className='navbarH'>
                <ul className='nav-list'>
                    <div className='logo'>
                        <li className='nav-item0'><img className='img' src={logo} alt='logo'></img></li>
                        <li className='nav-item1' ><a style={{color:'dodgerblue'}} href='#' >IDF-CONVEYANCE</a></li>
                    </div>
                <li className='nav-item6'><Link  href='#' to='/'>Home</Link></li>
                    <li className='nav-item5'><Link  href='#' to='/Map'>Map</Link></li>
                    <li className='nav-item2'><Link  href='#' to='/ticket'>Ticket</Link></li>
                    <li className='nav-item2'><Link  href='#' to='/real time'>Schedule Real Time</Link></li>
                    <li className='nav-item3'><Link  href='#' to='/schedule'>Schedule</Link></li>
                <li className='nav-item4'><a  href='#'>Statistiques</a></li>
                    <li className='nav-item9'><Link href='#' to='/login'>Login</Link></li>
                    <li className='nav-item4'><Link to='/transport'>Transport</Link></li>


                    <li className='nav-item8'><a  href='#'>Autre</a></li>

            </ul>
            </div>
        </nav>




    );
};

/*<nav className="navbar navbar-expand-lg navbar-light bg-light">
    <a className="navbar-brand" href="#">Navbar</a>
    <button className="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNav"
            aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
        <span className="navbar-toggler-icon"></span>
    </button>
    <div className="collapse navbar-collapse" id="navbarNav">
        <ul className="navbar-nav">
            <li className="nav-item active">
                <a className="nav-link" href="#">Home <span className="sr-only">(current)</span></a>
            </li>
            <li className="nav-item">
                <a className="nav-link" href="#">Features</a>
            </li>
            <li className="nav-item">
                <a className="nav-link" href="#">Pricing</a>
            </li>
            <li className="nav-item">
                <a className="nav-link disabled" href="#">Disabled</a>
            </li>
        </ul>
    </div>
</nav>*/
