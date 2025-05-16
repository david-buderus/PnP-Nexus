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
import { UniverseView } from './components/UniverseView';
import { User } from './pages/user/user';
import { UserPreferences } from './pages/user/preferences';
import { Admin } from './pages/admin/admin';

/** The entry point of the webapp */
class App extends Component {
    render() {
        return (
            <MantineProvider defaultColorScheme='auto'>
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
                            <Route path='/user' element={<User />}> </Route>
                            <Route path='/preferences' element={<UserPreferences />}> </Route>
                            <Route path='/universe' element={<UniverseView><UniverseOverview /></UniverseView>} ></Route>
                            <Route path='/universe-creation' element={<UniverseCreation />}></Route>
                            <Route path='/about' ></Route>
                            <Route path='/items' element={<UniverseView><Items /></UniverseView>} ></Route>
                            <Route path='/weapons' element={<UniverseView><Weapons /></UniverseView>} ></Route>
                            <Route path='/shields' element={<UniverseView><Shields /></UniverseView>} ></Route>
                            <Route path='/armor' element={<UniverseView><ArmorOverview /></UniverseView>} ></Route>
                            <Route path='/jewellery' element={<UniverseView><JewelleryOverview /></UniverseView>}></Route>
                            <Route path='/upgrades' element={<UniverseView><UpgradeOverview /></UniverseView>}></Route>
                            <Route path='/materials' element={<UniverseView><MaterialOverview /></UniverseView>}></Route>
                            <Route path='/crafting-recipes' element={<UniverseView><CraftingRecipeOverview /></UniverseView>}></Route>
                            <Route path='/upgrade-recipes' element={<UniverseView><UpgradeRecipeOverview /></UniverseView>}></Route>
                            <Route path='/characters' element={<UniverseView><></></UniverseView>}></Route>
                            <Route path='/spells' element={<UniverseView><SpellOverview /></UniverseView>}></Route>
                            <Route path='/talents' element={<UniverseView><TalentOverview /></UniverseView>}></Route>
                            <Route path='/admin' element={<Admin />}> </Route>
                            <Route path='/users' element={<UserOverview />}> </Route>
                        </Route>
                        <Route path="/login" element={<Login />}> </Route>
                    </Routes>
                </Router>
            </MantineProvider>
        );
    }
}

export default App;
