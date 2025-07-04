import React, {useEffect, useState} from 'react';
import '../styles/BusSchedule.css'
import {BUSLIGNES} from "../constants/back";
import axios from "axios";
import {ROUTETERMINUS} from "../constants/back";



export default function BusSchedule() {
    // États pour les lignes de bus, les terminus de route et la ligne sélectionnée
    const [busLignes, setBusLignes] = useState([]);
    const [routeTerminus, setRouteTerminus] = useState([]);
    const [ligneSelected, setLigneSelected] = useState('');

    // Tableau des fichiers PDF avec les noms et les chemins
    const pdfs = [
        { name:"48 Avenue de la Porte d'Ivry, 75013 Paris" , pdf: 'horaires_ligne_B1_Porte d\'Ivry.pdf' },
        { name: "Cité Universitaire", pdf: 'horaires_ligne_B1_cité.pdf' },
        { name: "Chaymae Station", pdf: 'horaires_ligne_B2_ChaymaeStation.pdf' },
        { name: "X Station", pdf: 'horaires_ligne_B2_XStation.pdf' },
        { name: "A st", pdf: 'horaires_ligne_B3_AStation.pdf' },
        { name: "Z st", pdf: 'horaires_ligne_B3_ZStation.pdf' },
        { name: "Badr Station", pdf: 'horaires_ligne_L1_BadrStation.pdf' },
        { name: "Marouane Station", pdf: 'horaires_ligne_L1_MarouaneStation.pdf' },
    ];

    // Terminus par défaut
    const terminus='Z st';

    // Gestion des clics sur les lignes de bus
    const handleClick1 = (event) => {
        setLigneSelected(event.target.id);
        showTerminus(event.target.id);
    };

    // Affichage des terminus pour une ligne sélectionnée
    const showTerminus = async (ligne) => {
        await axios.get(ROUTETERMINUS+`/${ligne}`)
            .then(response => setRouteTerminus(response.data))
            .catch(error => console.error('Erreur lors de la récupération des données depuis le backend :', error));
    }

    // Gestion des clics sur les terminus pour ouvrir les fichiers PDF correspondants
    const handleClick2 = (terminus) => {
        // Recherche de l'objet PDF correspondant dans le tableau pdfs
        const selectedPdf = pdfs.find(pdf => pdf.name === terminus);
        if (selectedPdf) {
            // Si l'objet PDF correspondant est trouvé, ouverture du PDF dans une nouvelle fenêtre
            window.open(`/pdfFiles/${selectedPdf.pdf}`, '_blank');
        }
    }

    // Effet pour charger les lignes de bus au chargement du composant
    useEffect(() => {
        axios.get(BUSLIGNES)
            .then(response => setBusLignes(response.data))
            .catch(error => console.error('Erreur lors de la récupération des données depuis le backend :', error));
    }, []);

    // Rendu du composant
    return (
        <div className='busSchedule'>
            <div className='container2'>
                <h2>Choisissez la ligne :</h2>
                {busLignes.map((ligne, key) => (
                    <a className="lignesAnchor" href='#' id={ligne} onClick={(e) => handleClick1(e)}>{ligne}</a>
                ))}
                <h3>Choisissez votre terminus :</h3>
                {routeTerminus.map((terminus, key) => (
                    <a className="lignesAnchor" href='' id={terminus} onClick={(e) => handleClick2(terminus)}>{terminus}</a>
                ))}
            </div>
            <div className='container3'>
                <p id='traficParagraph'>Veuillez noter que ces horaires sont théoriques et peuvent varier en fonction du trafic quotidien.</p>
            </div>
        </div>
    );
}





