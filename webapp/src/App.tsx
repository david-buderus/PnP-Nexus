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
                        <Route path='/universe'  element={< Universe />}></Route>
                        <Route path='/about' element={< About />}></Route>
                        <Route path='/items' element={< ItemPage />}></Route>
                        <Route path='/weapons' element={< WeaponPage />}></Route>
                        <Route path='/shields' element={< ShieldPage />}></Route>
                        <Route path='/armor' element={< ArmorPage />}></Route>
                        <Route path='/jewellery' element={< JewelleryPage />}></Route>
                    </Route>
                </Routes>
            </Router>
        );
    }
}

export default App;
