import React from 'react';
import '../styles/App.css';
import { FaInstagram, FaFacebook, FaGithub } from 'react-icons/fa';
import {FaReact} from 'react-icons/fa'

export default function App() {
  return (
    <div className="App" style={{margin:'0'}}>
      <main className='main'>
          <div className='container'>
              <h1 id="title1">SIRIUS</h1>
              <h1 id="title2">IDF-CONVEYANCE</h1>
          </div>
          <p>IDF-CONVEYANCE is a system being developed by students to optimize routes,
              track buses and tramways in real time, provide information to users,
              manage traffic intelligently, and promote sustainable practices to improve
              the passenger experience on the Paris transport network.
              .</p>
      </main>
        <div className='footer1'>
            <h1>CONTACT US:</h1>
            <a href="https://www.instagram.com/badr_abbady/" target="_blank"><FaInstagram size={60} style={{ marginRight: '5px', marginBottom: '80px'}} /></a>
            <a href="https://www.facebook.com/sir.bhalek.ila.bghi.tsre9.had.facebok/" target="_blank"><FaFacebook size={60} style={{ marginRight: '10px'}} /></a>
            <a href="https://github.com/BadrAbbady" target="_blank"><FaGithub size={60} style={{ marginRight: '10px'}} /></a>
        </div>
        <div className='footer2'>
            <h1 ><FaReact/> Powered By React.</h1>
        </div>
    </div>

  );
}
