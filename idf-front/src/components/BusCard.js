/* global google */


import React, {useEffect, useRef, useState} from 'react';
import {DirectionsRenderer, GoogleMap, InfoBox, Marker, useJsApiLoader} from "@react-google-maps/api";
import axios from "axios";
import {
    CORRESPANDONCEPASSAGE,
    CORRESPANDONCEPASSAGEBYLiGNE,
    DEPARTUREANDARRIVALTIME,
    NEXTPASSAGES,
    STATIONS,
    STATIONSBETWEEN,
    STATIONSLIGNECODE,
    TERMINUSOFTWOSTATIONS,
    TIMEBETWEENTWOSTATIONS
} from "../constants/back";
import autoBus from '../assets/autobus.png';
import mapStyle from '../assets/mapStyle.json';
import '../styles/BusCard.css';
import {Button} from "react-bootstrap";
import {IoTicket} from "react-icons/io5";
import {TbPointFilled} from "react-icons/tb";
import {PiWaveformFill} from "react-icons/pi";
import {IoIosArrowDown, IoIosArrowUp} from "react-icons/io";
import lignesColors from '../constants/lignesColors.json';
import { GiFlyingFlag } from "react-icons/gi";
import flag from '../assets/finish.png';
import pin from '../assets/pin.png'
import {LigneLogo} from "./LigneLogos";



const libraries=['places'];

export default function BusCard() {
    const configMap={
        googleMapsApiKey: "AIzaSyChirPoyOVg1cuEmCRV9CZ3naRvjZuZZaA",
        libraries:libraries
    }


    const {isLoaded} = useJsApiLoader(configMap);
    /*Réponse du service directions de google maps*/
    const [directionResponse, setDirectionResponse] = useState(null);
    /*Suivi de l'ensemble des stations station data*/
    const [station, setStation] = useState([]);
    /*Suivi des cordonnées de point de départ*/
    const [originCoordinate, setOriginCoordinate] = useState({});
    /*Suivi des cordonnées de point d'arrivée*/
    const [destinationCoordinate, setDestinationCoordinate] = useState({});
    const [allPassageCoordonates,setAllPassageCoordonates]=useState([]);
    /*Suivi des stations entre le point de départ et d'arrivée*/
    const [wayPoints, setWayPoints] = useState([]);
    /*Suivi des positions de bus par l'index de leur position*/
    const [busPositionIndex, setBusPositionIndex] = useState(0);
    /*Suivi des petits latitude et longitude entre chaque point de départ et d'arrivée*/
    const [routeCoordinates, setRouteCoordinates] = useState([]);
    /*Suivi du changement de ligne du trajet emprunté*/
    const [ligneCode,setLigneCode]=useState([]);
    /*Suivi des durées de chaque trajet*/
    const [routeDuration,setRouteDurations]=useState([]);
    /*État pour suivre si l'InfoWindow doit être affiché sur la station hovered*/
    const [hoveredStation, setHoveredStation] = useState(null);
    const departureStationRef = useRef();
    const arrivalStationRef = useRef();
    /*definition du timeout en fonction de la distance du trajet*/
    //const timeOut=((routeDuration/routeCoordinates.length)*60*1000);
    let localTime =new Date();
    let formattedCurrentTime = localTime.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    const [timePrint, setTimePrint] = useState(formattedCurrentTime);
    const [nextPassages, setNextPassages]=useState([]);
    const [ourTerminus,setOurTerminus]=useState([]);
    const [passageStations,setPassageStations]=useState([]);
    const [departureStationValue, setDepartureStationValue] = useState('');
    const [arrivalStationValue, setArrivalStationValue] = useState('');
    const [correspondancePassage,setCorrespandoncePassage]=useState({});
    const [wayPointsCorrespondance,setWayPointsCorrespondance]=useState([]);
    const [loading, setLoading] = useState(false);
    const [Allinformations , setAllInformations] = useState(null);





















    /*Un useEffect pour récuperer les données des stations du backend*/
    useEffect(() => {
        axios.get(STATIONS)
            .then(response => setStation(response.data))
            .catch(error => console.error('Error fetching stationsData from the backend:', error));
    }, []);




    // /*Un useEffect pour mettre à jour la position du bus en temps réel*/
    // useEffect(() => {
    //     if (routeCoordinates){
    //         const interval = setInterval(() => {
    //             setBusPositionIndex((prevIndex) => {
    //                 /*Incrémente l'index pour passer au point de correspondance suivant*/
    //                 const nextIndex = prevIndex + 1;
    //                 /*Si l'index dépasse la longueur des points de correspondance, réinitialise à zéro*/
    //                 return nextIndex < routeCoordinates.length ? nextIndex : 0;
    //             });
    //         }, 2000); /*Interval de 3 secondes pour simuler le déplacement du bus*/
    //         return () => clearInterval(interval); /*Nettoyage lorsque le composant est démonté*/
    //     }
    // }, [routeCoordinates]);






    /*Si la map est chargée*/
    if (isLoaded) {


        /*Fonction pour récuperer le code de la ligne des stations choisis et ne fonctionne que s'il y a une DirectionResponse*/
        const ligneCodeOfRoute = async (departStation, arrivStation) => {
            try {
                const response = await axios.get(STATIONSLIGNECODE + `/${departStation}/${arrivStation}`);
                return response.data;
            } catch (error) {
                console.error('Error fetching Terminus from the backend:', error);
                return null; // Ou renvoyez une valeur par défaut appropriée en cas d'erreur
            }
        }

        /*Fonction pour récuperer les durées entre les stations choisis et ne fonctionne que s'il y a une DirectionResponse*/
        const routeDurationCount = async (departStation, arrivStation) => {
            try {
                const response = await axios.get(TIMEBETWEENTWOSTATIONS + `/${departStation}/${arrivStation}`)
                ;
                return response.data;
            } catch (error) {
                console.error('Error fetching Terminus from the backend:', error);
                return null; // Ou renvoyez une valeur par défaut appropriée en cas d'erreur
            }
        }

        const departureANDarrivalTime = async (departStation, arrivStation) => {
            try {
                const response = await axios.get(DEPARTUREANDARRIVALTIME + `/${departStation}/${arrivStation}`)
                ;
                return response.data;
            } catch (error) {
                console.error('Error fetching Terminus from the backend:', error);
                return null; // Ou renvoyez une valeur par défaut appropriée en cas d'erreur
            }
        }


        const sumOfMinutes = () => {
            let list=[];
            if (Allinformations) {
                Allinformations.map((info) => {
                list.push([...info.duration]);
            })}
            let sumSubList = list.map(sousListe => sousListe.reduce((a, b) => a + b, 0));
            return sumSubList.reduce((a, b) => a + b, 0);
        }




        function getColorOfLine(line, lineColors) {
            if (lineColors.hasOwnProperty(line)) {
                return lineColors[line];
            } else {
                return "unknown";
            }
        }





        /*Fonction pour gérer l'appel de showRoute et s'assurer que toutes les données nécessaires sont disponibles*/
        const handleShowRoute = async () => {
                /*Récupérer tout les cordonnées des stations de passage*/
                await allPassageStationsCoordonates();

                /*Récupérer toutes les stations de passage*/
                await allPassageStations();

                const result=[];

            // Parcourir chaque ligne de transport
            for (let ligne in correspondancePassage) {

                // Obtenir la première et la dernière station de la ligne
                const stations = correspondancePassage[ligne];
                const departureStation = stations[0].stationName;
                const arrivalStation = stations[stations.length - 1].stationName;

                // Récupérer le terminus
                const terminus = await getTerminus(departureStation, arrivalStation);

                // Récupérer les prochains passages
                const nextPassages = await NextPassages(formattedCurrentTime, departureStation, arrivalStation);

                // Récupérer les between stations
                const stBetween = await getBeetwenStations(departureStation,arrivalStation);

                // Récupérer les codes des lignes
                const ligneCode = await ligneCodeOfRoute(departureStation,arrivalStation);

                // Récupérer les durations entre stations
                const routeDuration = await routeDurationCount(departureStation,arrivalStation);

                // Récupérer l'heure de départ et d'arrivée
                const departANDarrivTime= await departureANDarrivalTime(departureStation,arrivalStation);

                // Récupérer les directions
                const drct= await showRoute(stations,stBetween);

                // Récupérer la couleur de la ligne.
                const color= await getColorOfLine(ligne,lignesColors)




                // Récupérer le code de la ligne
                const lineResult = {
                    code: ligne,
                    terminus: terminus,
                    nextPassage: nextPassages,
                    depart: departureStation,
                    arriv: arrivalStation,
                    stationsBetween: stBetween,
                    ligneCode : ligneCode,
                    duration: routeDuration,
                    departureANDarrivalTime: departANDarrivTime,
                    directionResponse : drct,
                    color: color
                };
                result.push(lineResult);
            }
            setAllInformations(result);
            console.log(Allinformations);
            console.log(allPassageCoordonates)




            }


        const showRoute = async (stations, stBetween) => {
            let rslt= null;
            // If the input stations are null, do nothing
            if (departureStationValue === '' || arrivalStationValue === '') {
                return;
            }

            const directionService = new google.maps.DirectionsService();

            // If the destination and origin coordinates are not null, retrieve the result
            if (destinationCoordinate && originCoordinate) {
                rslt = await new Promise((resolve, reject) => {
                    directionService.route({
                        origin: { lat: stations[0].latitude, lng: stations[0].longitude },
                        destination: { lat: stations[stations.length - 1].latitude, lng: stations[stations.length - 1].longitude },
                        travelMode: google.maps.TravelMode.BICYCLING,
                        waypoints: stBetween.map(wayPoint => ({
                            location: { lat: wayPoint.latitude, lng: wayPoint.longitude }
                        })),
                    }, (result, status) => {
                        if (status === google.maps.DirectionsStatus.OK) {
                            extractRouteCoordinates(result);
                            resolve(result);
                        } else {
                            console.error('Error while fetching directions:', status);
                            reject(status);
                        }
                    });
                });
            }
            return rslt;
        };





        /*Fonction pour extraires les petits points de chaque trajet tel que result refére au resultat de google maps direction service*/
        const extractRouteCoordinates = (result) => {
            const route = result.routes[0];
            const routeCoordinates = [];

            for (let i = 0; i < route.legs.length; i++) {
                const steps = route.legs[i].steps;
                for (let j = 0; j < steps.length; j++) {
                    const path = steps[j].path;
                    for (let k = 0; k < path.length; k++) {
                        routeCoordinates.push({
                            lat: path[k].lat(),
                            lng: path[k].lng()
                        });
                    }
                }
            }
            setRouteCoordinates(routeCoordinates);
        };


        /*Fonction pour supprimer le trajet actuelle*/
        const clearRoute = () => {
            setDirectionResponse(null);
            departureStationRef.current.value='';
            arrivalStationRef.current.value='';
            setRouteCoordinates([]);
            setDestinationCoordinate(null);
            setOriginCoordinate(null);
            setRouteDurations(null);
            setLigneCode(null);
            setAllInformations(null);
            setWayPointsCorrespondance(null);
        }


        /*Fonction pour récuperer les stations entre le point de départ et d'arrivée et passer leur latitude et longitude à waypoints*/
        const getBeetwenStations =  async (depart,arriv) => {
            try {
                const response = await axios.get(STATIONSBETWEEN + `/${depart}/${arriv}`);
                return response.data;
            } catch (error) {
                console.error('Error fetching Terminus from the backend:', error);
                return null; // Ou renvoyez une valeur par défaut appropriée en cas d'erreur
            }
        }


        /*simple fonction pour récuperer tout les stations d'un trajet pour personnaliser les markers chaque station*/
        const allPassageStationsCoordonates = () => {
            const passageCoord= [
                ...wayPointsCorrespondance.map(wayPoint => {
                    /*Extraction des valeurs de latitude et de longitude de chaque wayPoint*/
                    const { lat, lng } = wayPoint.location;
                    return { lat, lng };
                })
            ];
            setAllPassageCoordonates(passageCoord);
        }





        /*Fonction pour récuperer tout les stations d'un trajet pour personnaliser les infos de chaque station*/
        const allPassageStations = () => {
            /*Crée un tableau vide pour stocker les stations de passage*/
            const passageSt = [];
            /*Ajouter la station de départ*/
            if (originCoordinate) { /*Vérifie si les coordonnées de départ existent*/
                /*Recherche la station correspondant aux coordonnées de départ*/
                const departureStation = station.find(st => st.latitude === originCoordinate.lat && st.longitude === originCoordinate.lng);
                /*Vérifie si une station correspondante a été trouvée*/
                if (departureStation) {
                    /*Ajoute la station de départ au tableau avec son nom et ses coordonnées*/
                    passageSt.push({ name: departureStation.stationName, position: originCoordinate });
                }
            }
            /*Ajouter les waypoints*/
            wayPointsCorrespondance.forEach(wayPoint => { /*Parcourt chaque waypoint*/
                /*Recherche la station correspondant aux coordonnées du waypoint*/
                const passingStation = station.find(st => st.latitude === wayPoint.location.lat && st.longitude === wayPoint.location.lng);
                /*Vérifie si une station correspondante a été trouvée*/
                if (passingStation) {
                    /*Ajoute la station de passage au tableau avec son nom et ses coordonnées*/
                    passageSt.push({ name: passingStation.stationName, position: wayPoint.location });
                }
            });
            /*Ajouter la station d'arrivée*/
            if (destinationCoordinate) { /*Vérifie si les coordonnées de destination existent*/
                /*Recherche la station correspondant aux coordonnées de destination*/
                const arrivalStation = station.find(st => st.latitude === destinationCoordinate.lat && st.longitude === destinationCoordinate.lng);
                /*Vérifie si une station correspondante a été trouvée*/
                if (arrivalStation) {
                    /*Ajoute la station d'arrivée au tableau avec son nom et ses coordonnées*/
                    passageSt.push({ name: arrivalStation.stationName, position: destinationCoordinate });
                }
            }
            /*Retourne le tableau des stations de passage*/
            setPassageStations(passageSt);
        }


        /*Définir une fonction pour gérer le survol d'un marqueur*/
        const handleMarkerHover = (station) => {
            setHoveredStation(station);
        }

        const  TimeAndPriceComponent= ({durationsList}) => {
            const newTime = new Date(localTime);
            let sumMinutes=0;
            if (durationsList) {
                sumMinutes=durationsList.reduce((sum, value) => sum + value, 0);
            }
            newTime.setMinutes(newTime.getMinutes() + sumMinutes);
            // Formater l'heure actuelle et l'heure calculée
            const formattedNewTime = newTime.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
            return (
                <div style={{height:'100%',width:'100%'}}>
                    <p>{formattedCurrentTime} - {formattedNewTime}</p>
                    <IoTicket style={{color:"dodgerblue",fontSize:'150%',margin:0,marginLeft:'25%',marginBottom:'2%'}}/>
                    <p>2.15 Є</p>
                </div>
            )
        }


        const NextPassages = async (time, departureStation, arrivalStation) => {
            try {
                const response = await axios.get(`${NEXTPASSAGES}/${time}/${departureStation}/${arrivalStation}`);
                return response.data;
            } catch (error) {
                console.error('Error fetching NextPassages from the backend:', error);
                return null; // Ou renvoyez une valeur par défaut appropriée en cas d'erreur
            }
        }




        const getTerminus = async (departureStation, arrivalStation) => {
            try {
                const response = await axios.get(`${TERMINUSOFTWOSTATIONS}/${departureStation}/${arrivalStation}`);
                return response.data;
            } catch (error) {
                console.error('Error fetching Terminus from the backend:', error);
                return null; // Ou renvoyez une valeur par défaut appropriée en cas d'erreur
            }
        }



        const getAllPassageStations =  (depart,arriv) => {
            axios.get(CORRESPANDONCEPASSAGE + `/${depart}/${arriv}`)
                .then(response => {
                    const waypointsData = response.data.map(station => ({
                        location: {lat: station.latitude, lng: station.longitude}
                    }));
                    setWayPointsCorrespondance(waypointsData);
                })
                .catch(error => console.error('Error fetching betweenStations from the backend:', error));
        }

        const waitForData = () => {
            setLoading(true); // Désactiver le bouton
            // Simuler un chargement de données pendant 1.5 secondes
            setTimeout(() => {
                setLoading(false); // Activer le bouton après 10 secondes
            }, 1500);
        };

        const getAllPassageStationsCorrespondance =  (departureStation,arrivalStation) => {
             axios.get(`${CORRESPANDONCEPASSAGEBYLiGNE}/${departureStation}/${arrivalStation}`)
                .then(response => setCorrespandoncePassage(response.data))
                .catch(error => console.error('Error fetching Terminus from the backend:', error));
        }










        return (
        <div className="busCard">
            <div className='routeDiv1'>
                <h1 style={{color:'dodgerblue',width:'70%',margin:'0 auto',marginTop:'20px'}}>Where are we going ?</h1>
                <div className='containeer1'>
                    <div className="form-floating mb-3" id="departureStation">
                        <input list='addresses'  className="form-control" id="floatingInput" placeholder="name@example.com" ref={departureStationRef} onChange={(event) => {
                            setDepartureStationValue(event.target.value);
                            const selectedStation = station.find(option => option.stationName === event.target.value);
                            if (selectedStation) {
                                setOriginCoordinate({
                                    lat: selectedStation.latitude,
                                    lng: selectedStation.longitude
                                });
                                waitForData();
                            }
                            getAllPassageStationsCorrespondance(departureStationRef.current.value,arrivalStationRef.current.value);
                            getAllPassageStations(departureStationRef.current.value,arrivalStationRef.current.value);
                        }}/>
                        <label htmlFor="floatingInput" id='depart'>Departure Station</label>
                        <datalist id="addresses">
                            {station.map((option, index) => (
                                <option key={index} value={option.stationName}></option>
                            ))}
                        </datalist>
                    </div>
                    <div className='deco'>
                        <div className="div-avec-ligne"></div>
                    </div>
                    <div className="form-floating mb-3" id="arrivalStation">
                        <input list='addresses' className="form-control" id="floatingInput"
                               placeholder="name@example.com" ref={arrivalStationRef} onChange={(event) => {
                            setArrivalStationValue(event.target.value);
                            const selectedStation = station.find(option => option.stationName === event.target.value);
                            if (selectedStation) {
                                setDestinationCoordinate({
                                    lat: selectedStation.latitude,
                                    lng: selectedStation.longitude
                                });
                                waitForData();
                            }
                            getAllPassageStationsCorrespondance(departureStationRef.current.value,arrivalStationRef.current.value);
                            getAllPassageStations(departureStationRef.current.value,arrivalStationRef.current.value);
                        }}/>
                        <label htmlFor="floatingInput" id='depart'>Arrival Station</label>
                        <datalist id="addresses">
                            {station.map((option, index) => (
                                <option key={index} value={option.stationName}></option>
                            ))}
                        </datalist>
                        <Button style={{backgroundColor:'#1E2329',width:'60%',marginLeft:'20%',marginTop:'40px',marginBottom:'20px',color:'dodgerblue',borderColor:'white'}} onClick={clearRoute}>Clear</Button>
                        <Button style={{backgroundColor:'#1E2329',width:'60%',marginLeft:'20%',marginTop:'40px',marginBottom:'20px',color:'dodgerblue',borderColor:'white'}} onClick={handleShowRoute} disabled={loading}>{loading ? 'Loading...' : "Let's Go !"}</Button>
                </div>
                </div>
                <div className="ligne-pointillee"></div>
                {Allinformations &&
                <div className='containeer2'>
                    <div className="ligneInformations">
                        <div className="ligneAndDuration">
                                <p>Ligne :</p>
                            {Allinformations && Allinformations.map((info,key) => (
                                <p style={{marginLeft:'1%'}}>{info.ligneCode}</p>
                            ))}
                                <p style={{marginLeft:'30%'}}>{sumOfMinutes()} minutes</p>
                        </div>
                        <div className="timeAndPrice">
                            {/*{Allinformations.map((info,key)=> (*/}
                            {/*    <TimeAndPriceComponent durationsList={info.durationj}/>*/}
                            {/*))}*/}
                        </div>
                        <div className="ligne-pointillee2"></div>
                        <div className="passageInformations">
                            {Allinformations && Allinformations.map((info,key) => (
                                    <div className='section'>
                                        <LigneLogo color={info.color} ligne={info.ligneCode}/>
                                        <p style={{color:"white",display:'inline-block',fontSize:'120%'}}>{info.depart}</p>

                                        <p style={{color:"white",display:'inline-block',fontSize:'120%',marginLeft:'15%'}}>{info.departureANDarrivalTime[0]}</p>
                                        <div className='sectionTable'>
                                            <div style={{height:'100%',width:'100%'}}>
                                                <NextPassageTableComponent listPassage={info.nextPassage} terminus={info.terminus} nextPassage={NextPassages} depart={info.depart} arriv={info.arriv} ligneColor={info.color}/>
                                                    <ShowStations betweenStations={info.stationsBetween} durations={info.duration} ligneColor={info.color}/>
                                            </div>
                                        </div>
                                        <LigneLogo color={info.color} ligne={info.ligneCode}/>
                                        <p style={{color:"white",display:'inline-block',fontSize:'120%'}}>{info.arriv}</p>
                                        <p style={{color:"white",display:'inline-block',fontSize:'120%',marginLeft:'15%'}}>{info.departureANDarrivalTime[1]}</p>
                                    </div>
                                ))}
                        </div>
                    </div>
                </div>}
            </div>

            <div className="myMapDiv">
                <GoogleMap
                    options={{
                        styles:mapStyle
                    }}
                    center={routeCoordinates.length > 0 ? routeCoordinates[busPositionIndex] : {lat:48.866667,lng:2.333333}}
                    zoom={13}
                    mapContainerStyle={{ height: "100%", width: "100%"  ,borderRadius:"50px"}}>
                    {Allinformations && Allinformations.map((info) => (
                            <DirectionsRenderer directions={info.directionResponse} options={{ suppressMarkers: true, polylineOptions: { strokeColor: info.color }}} />
                        ))}
                    {Allinformations && allPassageCoordonates.slice(1,-1).map((st,key)=> (
                        <Marker position={st} icon={{
                            path: window.google.maps.SymbolPath.CIRCLE, // Utilisez une forme personnalisée, comme un cercle
                            fillColor: '#0056d6',
                            fillOpacity: 1,
                            scale: 6, // Taille de la forme
                            strokeWeight: 0,// Pas de bordure
                        }} onMouseOver={() => handleMarkerHover(st)}
                                onMouseOut={() => handleMarkerHover(null)}>
                        </Marker>))}
                    {Allinformations &&
                        <Marker position={allPassageCoordonates[0]} icon={pin}
                                onMouseOver={() => handleMarkerHover(allPassageCoordonates[0])}
                                onMouseOut={() => handleMarkerHover(null)}>
                        </Marker>}
                    {Allinformations &&
                        <Marker position={allPassageCoordonates[allPassageCoordonates.length-1]} icon={flag}
                            onMouseOver={() => handleMarkerHover(allPassageCoordonates[allPassageCoordonates.length-1])}
                                onMouseOut={() => handleMarkerHover(null)}>
                        </Marker>}
                    {hoveredStation &&
                        <InfoBox
                            position={hoveredStation}
                            options={{ closeBoxURL: '', enableEventPropagation: true }}
                        >
                            <div style={{backgroundColor:"white"}}>
                                <h1>{passageStations.find(station =>
                                    station.position.lat === hoveredStation.lat &&
                                    station.position.lng === hoveredStation.lng
                                ).name}</h1>
                                <p>Description de la station...</p>
                            </div>
                        </InfoBox>
                    }
                    {/*{routeCoordinates.length > 0 && (*/}
                    {/*    <Marker position={routeCoordinates[busPositionIndex]} icon={*/}
                    {/*        autoBus}>*/}
                    {/*    </Marker>)*/}
                    {/*}*/}
                </GoogleMap>
            </div>
            </div>
            )
            }



}

const NextPassageTableComponent = ({listPassage,terminus,ligneColor}) => {


    const localTime =new Date();
    const formattedCurrentTime = localTime.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });


    return (
        <div className="passageTable" style={{borderLeft: `4px dotted ${ligneColor}`}}>
        <div style={{backgroundColor:'dodgerblue',borderTopRightRadius:'15px'}}>
            <p style={{fontFamily:'monospace',color:'white',fontSize:'110%',display:"inline-block",padding:'4px'}}>Next Passages</p>
            <p style={{fontFamily:'monospace',color:'white',fontSize:'110%',display:"inline-block",marginLeft:'10%',padding:'4px'}}>Update in {formattedCurrentTime}</p>
        </div>

        <div style={{backgroundColor:'white'}}>
            <section>
            <p style={{fontFamily:'monospace',color:'dodgerblue',fontSize:'110%',padding:'4px',overflow:'hidden',whiteSpace:'nowrap',textOverflow:'ellipsis'}}>{terminus}</p>
            </section>
            <section>
                <PiWaveformFill style={{color:'dodgerblue',fontSize:'150%',margin:'0',display:'inline-block',marginLeft:'10%'}}/>
                <p style={{fontFamily:'monospace',color:'dodgerblue',fontSize:'110%',display:"inline-block",padding:'4px'}}>{listPassage[0]}</p>
            </section>
        </div>
        <div style={{backgroundColor:'dodgerblue'}}>
            <section>
                <p style={{fontFamily:'monospace',color:'white',fontSize:'110%',padding:'4px',overflow:'hidden',whiteSpace:'nowrap',textOverflow:'ellipsis'}}>{terminus}</p>
            </section>
            <section>
                <PiWaveformFill style={{color:'white',fontSize:'150%',margin:'0',display:'inline-block',marginLeft:'10%'}}/>
                <p style={{fontFamily:'monospace',color:'white',fontSize:'110%',display:"inline-block",padding:'4px'}}>{listPassage[1]}</p>
            </section>
        </div>
        <div style={{backgroundColor:'white'}}>
            <section>
                <p style={{fontFamily:'monospace',color:'dodgerblue',fontSize:'110%',padding:'4px',overflow:'hidden',whiteSpace:'nowrap',textOverflow:'ellipsis'}}>{terminus}</p>
            </section>
            <section>
                <PiWaveformFill style={{color:'dodgerblue',fontSize:'150%',margin:'0',display:'inline-block',marginLeft:'10%'}}/>
                <p style={{fontFamily:'monospace',color:'dodgerblue',fontSize:'110%',display:"inline-block",padding:'4px'}}>{listPassage[2]}</p>
            </section>
        </div>
    </div>)

}


const ShowStations = ({betweenStations,durations,ligneColor}) => {

    const [showStations,setShowStations]=useState(false);

    const sumStationsDurations = () => {
        let sum = 0;
        if (durations.length > 3) {
            for (let i = 1; i < durations.length - 1; i++) {
                sum += durations[i];
            }
        }
        else if (durations.length===3) {sum=durations[1]}
        return sum;
    }

    const sum = sumStationsDurations();

    const handleStationsVisibility = () => {
        setShowStations(!showStations)
    }

     if (betweenStations.length>0)
        return (
            <div className='sectionStations' style={{borderLeft:`4px dotted ${ligneColor}`}}>
                {showStations ? <IoIosArrowUp id='up' style={{
                    color: 'white',
                    margin: '0',
                    fontSize: '160%',
                    marginRight: '2%',
                    marginLeft: '10%'}}/> : <IoIosArrowDown id='up' style={{
                    color: 'white',
                    margin: '0',
                    fontSize: '160%',
                    marginRight: '2%',
                    marginLeft: '10%'
                }}/>}

                { betweenStations.length!==1 ? <a href='#up' style={{color: 'white', fontSize: '120%', textDecoration: 'none'}}
                   onClick={handleStationsVisibility}> {sum} min ({betweenStations.length} Stops) </a> : <a href='#up' style={{color: 'white', fontSize: '120%', textDecoration: 'none'}}
                                                                                                            onClick={handleStationsVisibility}> (1 Stop) </a> }
                {showStations && betweenStations.map((station, index) => (
                    <div style={{height: '100%', width: '100%', paddingLeft: '10%'}} key={index}>
                        <p style={{color: 'white', fontSize: '120%', marginLeft: '8%', marginTop: '4%'}}
                           key={index}>{station.stationName}</p>
                    </div>
                ))
                }
            </div>
        )

}


