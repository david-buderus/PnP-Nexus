import React, { Component } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/home';
import About from './pages/about';
import PageBase from './components/PageBase';
import Universe from './pages/universe';
import ItemPage from './pages/items/items';
import WeaponPage from './pages/items/weapons';
import ShieldPage from './pages/items/shields';
import ArmorPage from './pages/items/armor';
import JewelleryPage from './pages/items/jewellery';
import { Login } from './pages/login';
import { UserProfile, UserPreferences } from './pages/users/user';
import { Backup } from './pages/backup';
import { UserOverview } from './pages/users/user-overview';
import { Admin } from './pages/admin';
import { ItemTypePage } from './pages/items/item-types';
import { MaterialPage } from './pages/items/materials';

/** The entry point of the webapp */
class App extends Component {
    render() {
        return (
            <Router>
                <style>{`
                    body {
                        margin: 0px;
                        padding: 0px;
                    }
                `}
                </style>
                <Routes>
                    <Route path="/" element={<PageBase />} >
                        <Route path='/' element={< Home />}></Route>
                        <Route path='/user' element={< UserProfile />}> </Route>
                        <Route path='/preferences' element={< UserPreferences />}> </Route>
                        <Route path='/universe' element={< Universe />}></Route>
                        <Route path='/about' element={< About />}></Route>
                        <Route path='/items' element={< ItemPage />}></Route>
                        <Route path='/weapons' element={< WeaponPage />}></Route>
                        <Route path='/shields' element={< ShieldPage />}></Route>
                        <Route path='/armor' element={< ArmorPage />}></Route>
                        <Route path='/jewellery' element={< JewelleryPage />}></Route>
                        <Route path='/item-types' element={< ItemTypePage />}></Route>
                        <Route path='/materials' element={< MaterialPage />}></Route>
                        <Route path='/admin' element={< Admin />}> </Route>
                        <Route path='/users' element={< UserOverview />}> </Route>
                        <Route path='/backup' element={< Backup />}></Route>
                    </Route>
                    <Route path="/login" element={<Login />}> </Route>
                </Routes>
            </Router>
        );
    }
}

export default App;
