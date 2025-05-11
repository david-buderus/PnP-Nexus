import React, { Component } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import PageBase from './components/PageBase';
import Login from './pages/login';
import '@mantine/core/styles.css';
import 'mantine-react-table/styles.css';

import { MantineProvider } from '@mantine/core';
import Home from './pages/home';
import UniverseCreation from './pages/universe/universe-creation';
import UniverseOverview from './pages/universe/universe';
import { ArmorOverview, Items, JewelleryOverview, Shields, Weapons } from './pages/database/items/items';
import { UpgradeOverview } from './pages/database/items/upgrades';
import { MaterialOverview } from './pages/database/items/materials';
import { CraftingRecipeOverview } from './pages/database/crafting/crafting-recipes';
import { UpgradeRecipeOverview } from './pages/database/crafting/upgrade-recipes';
import { SpellOverview } from './pages/database/characters/spellts';
import { TalentOverview } from './pages/database/characters/talents';
import { UserOverview } from './pages/admin/user-overview';

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
                            <Route path='/universe' element={<UniverseOverview />} ></Route>
                            <Route path='/universe-creation' element={<UniverseCreation />}></Route>
                            <Route path='/about' ></Route>
                            <Route path='/items' element={<Items />} ></Route>
                            <Route path='/weapons' element={<Weapons />} ></Route>
                            <Route path='/shields' element={<Shields />} ></Route>
                            <Route path='/armor' element={<ArmorOverview />} ></Route>
                            <Route path='/jewellery' element={<JewelleryOverview />}></Route>
                            <Route path='/upgrades' element={<UpgradeOverview />}></Route>
                            <Route path='/materials' element={<MaterialOverview />}></Route>
                            <Route path='/crafting-recipes' element={<CraftingRecipeOverview />}></Route>
                            <Route path='/upgrade-recipes' element={<UpgradeRecipeOverview />}></Route>
                            <Route path='/characters' ></Route>
                            <Route path='/spells' element={<SpellOverview />}></Route>
                            <Route path='/talents' element={<TalentOverview />}></Route>
                            <Route path='/admin' > </Route>
                            <Route path='/users' element={<UserOverview />}> </Route>
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
