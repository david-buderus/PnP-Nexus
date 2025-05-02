import React, { Component } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import PageBase from './components/PageBase';
import Login from './pages/login';
import '@mantine/core/styles.css';

import { MantineProvider } from '@mantine/core';
import Home from './pages/home';
import UniverseCreation from './pages/universe/universe-creation';

/** The entry point of the webapp */
class App extends Component {
    render() {
        return (
            <MantineProvider>
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
                            <Route path='/' element={<Home />}></Route>
                            <Route path='/user'> </Route>
                            <Route path='/preferences'> </Route>
                            <Route path='/universe' ></Route>
                            <Route path='/universe-creation' element={<UniverseCreation />}></Route>
                            <Route path='/about' ></Route>
                            <Route path='/items' ></Route>
                            <Route path='/weapons' ></Route>
                            <Route path='/shields' ></Route>
                            <Route path='/armor' ></Route>
                            <Route path='/jewellery'></Route>
                            <Route path='/upgrades' ></Route>
                            <Route path='/materials' ></Route>
                            <Route path='/crafting-recipes'></Route>
                            <Route path='/upgrade-recipes' ></Route>
                            <Route path='/characters' ></Route>
                            <Route path='/spells' ></Route>
                            <Route path='/talents'></Route>
                            <Route path='/primary-attributes'></Route>
                            <Route path='/secondary-attributes'></Route>
                            <Route path='/admin' > </Route>
                            <Route path='/users' > </Route>
                            <Route path='/backup'></Route>
                        </Route>
                        <Route path="/login" element={<Login />}> </Route>
                    </Routes>
                </Router>
            </MantineProvider>
        );
    }
}

export default App;
