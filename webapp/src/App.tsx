import React, {Component} from 'react';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import PageBase from './components/PageBase';
import Login from './pages/login';
import '@mantine/core/styles.css';
import '@mantine/tiptap/styles.css';

import {MantineProvider} from '@mantine/core';
import Home from './pages/home';
import UniverseCreation from './pages/universe/universe-creation';
import UniverseOverview from './pages/universe/universe';
import {ArmorOverview, Items, JewelleryOverview, Shields, Weapons} from './pages/database/items/items';
import {UpgradeOverview} from './pages/database/items/upgrades';
import {MaterialOverview} from './pages/database/items/materials';
import {CraftingRecipeOverview} from './pages/database/crafting/crafting-recipes';
import {UpgradeRecipeOverview} from './pages/database/crafting/upgrade-recipes';
import {SpellOverview} from './pages/database/characters/spells';
import {TalentOverview} from './pages/database/characters/talents';
import {UserOverview} from './pages/admin/user-overview';
import {UniverseView} from './components/UniverseView';
import {User} from './pages/user/user';
import {UserPreferences} from './pages/user/preferences';
import {Admin} from './pages/admin/admin';
import {NationView} from './pages/database/world/nations';
import {ReactRouter6Adapter} from 'use-query-params/adapters/react-router-6';
import {QueryParamProvider} from 'use-query-params';
import {SpeciesOverview} from './pages/database/world/species-overview';
import {SpeciesDetail} from './pages/database/world/species';
import {CharactersOverview} from './pages/database/characters/characters-overview';
import {CharacterSheetsOverview} from './pages/database/characters/character-sheets-overview';
import {CharacterSheetEditor} from './pages/database/characters/character-sheet-editor';

/** The entry point of the webapp */
class App extends Component {
    override render() {
        return (
            <MantineProvider defaultColorScheme="auto">
                <Router>
                    <style>{`
                    body {
                        margin: 0px;
                        padding: 0px;
                    }
                `}
                    </style>
                    <Routes>
                        <Route path="/" element={
                            <QueryParamProvider adapter={ReactRouter6Adapter}>
                                <PageBase/>
                            </QueryParamProvider>
                        }>
                            <Route path="/" element={<Home/>}></Route>
                            <Route path="/user" element={<User/>}> </Route>
                            <Route path="/preferences" element={<UserPreferences/>}/>
                            <Route path="/universe" element={<UniverseView><UniverseOverview/></UniverseView>}/>
                            <Route path="/universe-creation" element={<UniverseCreation/>}/>
                            <Route path="/about"/>
                            <Route path="/items" element={<UniverseView><Items/></UniverseView>}/>
                            <Route path="/weapons" element={<UniverseView><Weapons/></UniverseView>}/>
                            <Route path="/shields" element={<UniverseView><Shields/></UniverseView>}/>
                            <Route path="/armor" element={<UniverseView><ArmorOverview/></UniverseView>}/>
                            <Route path="/jewellery"
                                   element={<UniverseView><JewelleryOverview/></UniverseView>}/>
                            <Route path="/upgrades"
                                   element={<UniverseView><UpgradeOverview/></UniverseView>}/>
                            <Route path="/materials"
                                   element={<UniverseView><MaterialOverview/></UniverseView>}/>
                            <Route path="/crafting-recipes"
                                   element={<UniverseView><CraftingRecipeOverview/></UniverseView>}/>
                            <Route path="/upgrade-recipes"
                                   element={<UniverseView><UpgradeRecipeOverview/></UniverseView>}/>
                            <Route path="/characters"
                                   element={<UniverseView><CharactersOverview/> </UniverseView>}/>
                            <Route path="/characters-editor"
                                   element={<UniverseView><CharacterSheetsOverview/></UniverseView>}/>
                            <Route path="/characters-editor/:sheet"
                                   element={<UniverseView><CharacterSheetEditor/></UniverseView>}/>
                            <Route path="/spells" element={<UniverseView><SpellOverview/></UniverseView>}/>
                            <Route path="/talents" element={<UniverseView><TalentOverview/></UniverseView>}/>
                            <Route path="/species" element={<UniverseView><SpeciesOverview/></UniverseView>}/>
                            <Route path="/species/:species" element={<UniverseView><SpeciesDetail/></UniverseView>}/>
                            <Route path="/nations" element={<UniverseView><NationView/></UniverseView>}/>
                            <Route path="/nations/:nation" element={<UniverseView><NationView/></UniverseView>}/>
                            <Route path="/admin" element={<Admin/>}/>
                            <Route path="/users" element={<UserOverview/>}/>
                        </Route>
                        <Route path="/login" element={<Login/>}> </Route>
                    </Routes>
                </Router>
            </MantineProvider>
        );
    }
}

export default App;
