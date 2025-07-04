import React, {useEffect, useState} from "react";
import '../styles/BusSchedule.css'
import axios from "axios";
import {ROUTETERMINUS, SCHEDULESBYLIGNES, STATIONSBYLIGNES, TRAMLIGNES} from "../constants/back";


export default function TramSchedule() {
    // Définition des états pour les lignes de tram, la ligne sélectionnée, l'horaire de la ligne, les stations de la ligne et le terminus de la route
    const [tramLignes, setTramLignes] = useState([]);
    const [lignesSelected, setLigneSelected] = useState('');
    const [ligneSchedule, setLigneSchedule] = useState({});
    const [ligneStations, setLigneStations] = useState([]);
    const [routeTerminus, setRouteTerminus] = useState([]);

    // Tableau des fichiers PDF avec les noms et les chemins
    const pdfs = [
        { name: "Badr Station", pdf: 'horaires_ligne_L1_BadrStation.pdf' },
        { name: "Marouane Station", pdf: 'horaires_ligne_L1_MarouaneStation.pdf' }
    ];

    // Gestion des clics sur les lignes de tram
    const handleClick1 = (event) => {
        showTerminus(event.target.id);
    };

    // Affichage des terminus pour une ligne sélectionnée
    const showTerminus = async (ligne) => {
        await axios.get(ROUTETERMINUS + `/${ligne}`)
            .then(response => setRouteTerminus(response.data))
            .catch(error => console.error('Erreur lors de la récupération des données depuis le backend :', error));
    };

    // Gestion des clics sur les terminus pour ouvrir les fichiers PDF correspondants
    const handleClick2 = (terminus) => {
        // Recherche de l'objet PDF correspondant dans le tableau pdfs
        const selectedPdf = pdfs.find(pdf => pdf.name === terminus);
        if (selectedPdf) {
            // Si l'objet PDF correspondant est trouvé, ouverture du PDF dans une nouvelle fenêtre
            window.open(`/pdfFiles/${selectedPdf.pdf}`, '_blank');
        }
    };

    // Effet pour charger les lignes de tram au chargement du composant
    useEffect(() => {
        axios.get(TRAMLIGNES)
            .then(response => setTramLignes(response.data))
            .catch(error => console.error('Erreur lors de la récupération des données depuis le backend :', error));
    }, []);

    // Rendu du composant
    return (
        <div className='busSchedule'>
            <div className='container2'>
                <h2>Choisissez la ligne :</h2>
                {tramLignes.map((ligne, key) => (
                    <a className="lignesAnchor" href='#' id={ligne} onClick={(e) => handleClick1(e)}>{ligne}</a>
                ))}
                <h3>Choisissez votre terminus :</h3>
                {routeTerminus.map((terminus, key) => (
                    <a className="lignesAnchor" href='' id={terminus} onClick={(e) => handleClick2(terminus)}>{terminus}</a>
                ))}
            </div>
            <div className='container3'>
                <p id="traficParagraph">Veuillez noter que ces horaires sont théoriques et peuvent varier en fonction du trafic quotidien.</p>
            </div>
        </div>
    );
}
