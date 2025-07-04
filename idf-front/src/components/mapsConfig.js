/* global google */

import React, { useEffect, useRef, useState } from 'react';
import {useJsApiLoader, GoogleMap, DirectionsRenderer, Marker} from "@react-google-maps/api";
import { VscDiscard } from "react-icons/vsc";
import axios from "axios";
import { STATIONS ,STATIONSBETWEEN } from "../constants/back";
import { Button } from "react-bootstrap";
import '../styles/Map.css'



const libraries=['places'];


export default function MapConfig() {
    console.log('component trendrat')
    const configMap = {
        googleMapsApiKey: "AIzaSyChirPoyOVg1cuEmCRV9CZ3naRvjZuZZaA",
        libraries: libraries
    }

    const {isLoaded} = useJsApiLoader(configMap);

    /*Réponse du service directions de google maps*/
    const [directionResponse, setDirectionResponse] = useState([]);
    /*Suivi de l'ensemble des stations station data*/
    const [station, setStation] = useState([]);
    /*Suivi des cordonnées de point de départ*/
    const [originCoordinate, setOriginCoordinate] = useState({});
    /*Suivi des cordonnées de point d'arrivée*/
    const [destinationCoordinate, setDestinationCoordinate] = useState({});
    /*Suivi des stations entre le point de départ et d'arrivée*/
    const [wayPoints, setWayPoints] = useState([]);
    /*Suivi des positions de bus par l'index de leur position*/
    const [busPositionIndex, setBusPositionIndex] = useState(0);
    /*Suivi des petits latitude et longitude entre chaque point de départ et d'arrivée*/
    const [routeCoordinates, setRouteCoordinates] = useState([]);
    const departureStationRef = useRef();
    const arrivalStationRef = useRef();


    /*Un useEffect pour récuperer les données des stations du backend*/
    useEffect(() => {
        axios.get(STATIONS)
            .then(response => setStation(response.data))
            .catch(error => console.error('Error fetching data from the backend:', error));
    }, []);


    /*Un useEffect pour mettre à jour la position du bus en temps réel*/
    useEffect(() => {
        const interval = setInterval(() => {
            setBusPositionIndex((prevIndex) => {
                /*Incrémente l'index pour passer au point de correspondance suivant*/
                const nextIndex = prevIndex + 1;
                /*Si l'index dépasse la longueur des points de correspondance, réinitialise à zéro*/
                return nextIndex < routeCoordinates.length ? nextIndex : 0;
            });
        }, 3000); /*Interval de 3 secondes pour simuler le déplacement du bus*/
        return () => clearInterval(interval); /*Nettoyage lorsque le composant est démonté*/
    }, [routeCoordinates]);


    /*Si la map est chargée*/
    if (isLoaded) {

        /*Fonction pour gérer la réponse du service de direction de google maps*/
        const showRoute = async () => {
            /*Si les inputs des stations sont nulls , ne rien faire*/
            if (departureStationRef.current.value === '' || arrivalStationRef.current.value === '') {
                return
            }
            const directionService = await new google.maps.DirectionsService();
            /*Si les cordonnées de point de départ et d'arrivée sont non null , récuperer le resultat*/
            if (destinationCoordinate && originCoordinate) {
                directionService.route({
                    origin: originCoordinate,
                    destination: destinationCoordinate,
                    travelMode: google.maps.TravelMode.BICYCLING,
                    waypoints: wayPoints.map(wayPoint => ({location: wayPoint})),
                }, (result, status) => {
                    if (status === google.maps.DirectionsStatus.OK) {
                        setDirectionResponse(result);
                        const routeCoordinates = extractRouteCoordinates(result);
                    } else {
                        console.error('Error while fetching directions:', status);
                    }
                });
            }
        }


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
            departureStationRef.current.value = '';
            arrivalStationRef.current.value = '';
        }


        /*Fonction pour récuperer les stations entre le point de départ et d'arrivée et passer leur latitude et longitude à waypoints*/
        const getBeetwenStations = async () => {
            await axios.get(STATIONSBETWEEN + `/${departureStationRef.current.value}/${arrivalStationRef.current.value}`)
                .then(response => {
                    const waypointsData = response.data.map(station => ({
                        location: {lat: station.latitude, lng: station.longitude}
                    }));
                    setWayPoints(waypointsData);
                })
                .catch(error => console.error('Error fetching data from the backend:', error));
        }


    return (
        <div className="Map">
            <div className="container1">
                <div className="mapDiv">
                    <GoogleMap
                        center={routeCoordinates[busPositionIndex]}
                        zoom={15}
                        mapContainerStyle={{height: '100%', width: '100%', 'border-radius': '50px'}}>
                        {directionResponse && <DirectionsRenderer directions={directionResponse} options={{ suppressMarkers: true }} />}
                        {routeCoordinates.length > 0 && (
                        <Marker position={routeCoordinates[busPositionIndex]}>
                        </Marker>)}
                    </GoogleMap>
                </div>
                <div className="routeDiv">
                    <h1 style={{color: "white", marginLeft: "25%"}}>Where are we going ?</h1>
                    <div className='routeElements'>
                        <div className="form-floating mb-3">
                            <input list='addresses' className="form-control" id="floatingInput"
                                   placeholder="name@example.com" ref={departureStationRef} onChange={(event) => {
                                const selectedStation = station.find(option => option.stationName === event.target.value);
                                if (selectedStation) {
                                    setOriginCoordinate({
                                        lat: selectedStation.latitude,
                                        lng: selectedStation.longitude
                                    });
                                }
                            }}/>
                            <label htmlFor="floatingInput">Departure Station</label>
                            <datalist id="addresses">
                                {station.map((option, index) => (
                                    <option key={index} value={option.stationName}></option>
                                ))}
                            </datalist>
                        </div>
                        <div className="form-floating mb-3">
                            <input list='addresses' className="form-control" id="floatingInput"
                                   placeholder="name@example.com" ref={arrivalStationRef} onChange={(event) => {
                                const selectedStation = station.find(option => option.stationName === event.target.value);
                                if (selectedStation) {
                                    getBeetwenStations();
                                    setDestinationCoordinate({
                                        lat: selectedStation.latitude,
                                        lng: selectedStation.longitude
                                    });
                                }
                            }}/>
                            <label htmlFor="floatingInput">Arrival Station</label>
                            <datalist id="addresses">
                                {station.map((option, index) => (
                                    <option key={index} value={option.stationName}></option>
                                ))}
                            </datalist>

                        </div>
                    </div>
                    <Button  type='submit' onClick={showRoute}>Show Route</Button>
                    <Button onClick={clearRoute}><VscDiscard/></Button>
                </div>
            </div>
        </div>
    )
}


}