import React, { Component } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './pages/home';
import About from './pages/about';
import PageBase from './components/PageBase';
import Universe from './pages/universe';
import Items from './pages/items/items';

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
                        <Route path='/universe' element={< Universe />}></Route>
                        <Route path='/about' element={< About />}></Route>
                        <Route path='/items' element={< Items />}></Route>
                        <Route path='/weapons' element={< About />}></Route>
                        <Route path='/shields' element={< About />}></Route>
                        <Route path='/armor' element={< About />}></Route>
                        <Route path='/jewellery' element={< About />}></Route>
                    </Route>
                </Routes>
            </Router>
        );
    }
}

export default App;
