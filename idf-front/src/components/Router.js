import React from 'react';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import App from './App';
import NavbarHeader from './NavbarHeader';
import NotFound from './NotFound';
import TicketForm from './TicketForm';
import TicketDisplay from './TicketDisplay';
import BusCard from "./BusCard";
import MapConfig from "./mapsConfig";
import Schedule from "./Schedule";
import ScheduleRealTime from "./ScheduleRealTime"
import LoginComponent from "./LoginComponent";
import UserInfo from "./UserInfo";
import UserRegistries from "./UserRegistries";
import Transport from "./Transport";
export default function Router() {
    return (
        <BrowserRouter>
            <div>
                <NavbarHeader />
                <Routes>
                    <Route path="/" element={<App />} />
                    <Route path="/Map" element={<BusCard />} />
                    <Route path="/real time" element={<ScheduleRealTime />} />
                    <Route path="/ticket/:ticketId" element={<TicketDisplay />} />
                    <Route path="*" element={<NotFound />} />
                    <Route path="/schedule" element={<Schedule />} />                    <Route path="/login" element={<LoginComponent />}/>
                    <Route path="/user-info" element={<UserInfo/>}/>
                    <Route path="/user-registries" element={<UserRegistries/>}/>
                    <Route path="/transport" element={<Transport />} />



                </Routes>
            </div>
        </BrowserRouter>
    );
}
