import { AuthenticationServiceApi, Universe, UniverseServiceApi, UserServiceApi } from '../api';
import { Outlet, useOutletContext, useSearchParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { FaUser } from "react-icons/fa";
import { Autocomplete, Box, CssBaseline, TextField, Toolbar, styled } from '@mui/material';
import MainSidebar from './MainSidebar';
import { UserPermissions, extractUserPermissions } from './interfaces/UserPermissions';
import { API_CONFIGURATION } from './Constants';
import { NexusAppBar } from './NexusAppBar';
import { NexusSidebar } from './NexusSidebar';
import { useTranslation } from 'react-i18next';
import { TfiWorld } from 'react-icons/tfi';
import { GiAxeSword, GiChestArmor, GiRing, GiShield, GiSwapBag } from 'react-icons/gi';

type UniverseContext = { universes: Universe[], activeUniverse: Universe; setActiveUniverse: (activeUniverse: Universe) => void; };
type UserContext = { userPermissions: UserPermissions; };

const UNIVERSE_API = new UniverseServiceApi(API_CONFIGURATION);
const AUTHENTICATION_API = new AuthenticationServiceApi(API_CONFIGURATION);
const USER_API = new UserServiceApi(API_CONFIGURATION);

function PageBase() {
  const { t } = useTranslation();
  const [universes, setUniverses] = useState<Universe[]>([]);
  const [searchParams, setSearchParams] = useSearchParams();
  const [activeUniverse, setActiveUniverse] = useState<Universe>(null);
  const [username, setUsername] = useState<string>(null);
  const [userPermissions, setUserPermissions] = useState<UserPermissions>({
    isAdmin: false,
    canCreateUniverses: false,
    canReadActiveUniverse: false,
    canWriteActiveUniverse: false,
    isActiveUniverseOwner: false
  });
  const [open, setOpen] = useState(false);

  const handleDrawerChange = () => {
    setOpen(!open);
  };

  useEffect(() => {
    UNIVERSE_API.getAllUniverses().then(response => {
      setUniverses(response.data);
      const universe = response.data.find(u => u.name === searchParams.get("universe"));
      if (universe !== undefined) {
        setActiveUniverse(universe);
      }
    });
    AUTHENTICATION_API.getUsername().then(response => {
      setUsername(response.data);
    });
  }, []);

  useEffect(() => {
    if (activeUniverse) {
      searchParams.set("universe", activeUniverse.name);
      setSearchParams(searchParams);
    }
  }, [activeUniverse]);

  useEffect(() => {
    if (username === null) {
      return;
    }
    USER_API.getPermissions(username).then(response => {
      setUserPermissions(extractUserPermissions(response.data, activeUniverse));
    });
  }, [activeUniverse, username]);

  return <Box sx={{ display: 'flex' }}>
    <CssBaseline />
    <NexusAppBar universes={universes} activeUniverse={activeUniverse} setActiveUniverse={setActiveUniverse} />
    <NexusSidebar open={open} handleDrawerChange={handleDrawerChange} entries={[
      { id: "universe-menu", label: t("universe"), link: "/universe", icon: <TfiWorld /> },
      {
        id: "items-menu", label: t("items"), link: "/items", icon: <GiSwapBag />, subEntries: [
          { id: "weapons-menu", label: t("weapons"), link: "/weapons", icon: <GiAxeSword /> },
          { id: "shields-menu", label: t("shields"), link: "/shields", icon: <GiShield /> },
          { id: "armor-menu", label: t("armor"), link: "/armor", icon: <GiChestArmor /> },
          { id: "jewellery-menu", label: t("jewellery"), link: "/jewellery", icon: <GiRing /> }
        ]
      }
    ]} />
    <Box component="main" height="100vh" display="flex" flexDirection="column" padding={2}>
      <Toolbar />
      <Box flex={1} overflow="auto">
        <Outlet context={{ universes: universes, activeUniverse: activeUniverse, setActiveUniverse: setActiveUniverse, userPermissions: userPermissions }} />
      </Box>
    </Box>
  </Box>;
}

/** Returns context over the currently avaible universes. */
export function getUniverseContext() {
  return useOutletContext<UniverseContext>();
}

/** Returns context over the currently logged in user. */
export function getUserContext() {
  return useOutletContext<UserContext>();
}

export default PageBase;
