import { AuthenticationServiceApi, CurrencySettings, PnPUser, PnPUserPreference, Universe, UniverseServiceApi, UniverseSettingsServiceApi, UserServiceApi } from '../api';
import { Outlet, useOutletContext, useSearchParams } from 'react-router-dom';
import { useEffect, useState } from 'react';
import { Box, CssBaseline, ThemeProvider, Toolbar } from '@mui/material';
import { UserPermissions, extractUserPermissions } from './interfaces/UserPermissions';
import { API_CONFIGURATION, THEME } from './Constants';
import { NexusAppBar } from './NexusAppBar';
import { MenuEntryProps, NexusSidebar } from './NexusSidebar';
import { useTranslation } from 'react-i18next';
import { TfiWorld } from 'react-icons/tfi';
import { GiAxeSword, GiBurningBook, GiChestArmor, GiClayBrick, GiGearHammer, GiHeartInside, GiMagicAxe, GiMuscleUp, GiRing, GiShield, GiSpellBook, GiStoneCrafting, GiSupersonicArrow, GiSwapBag } from 'react-icons/gi';
import { FaPersonRays } from "react-icons/fa6";
import i18n from '../i18n';
import { IoSettingsSharp } from 'react-icons/io5';
import { HiUserCircle } from 'react-icons/hi2';

type UniverseContext = { universes: Universe[], activeUniverse: Universe, setActiveUniverse: (activeUniverse: Universe) => void, fetchUniverses: () => Promise<void>, currencySettings: CurrencySettings; };
type UserContext = { userPermissions: UserPermissions, userPreferences: PnPUserPreference, user: PnPUser, refreshUser: () => void; };

const UNIVERSE_API = new UniverseServiceApi(API_CONFIGURATION);
const SETTINGS_API = new UniverseSettingsServiceApi(API_CONFIGURATION);
const AUTHENTICATION_API = new AuthenticationServiceApi(API_CONFIGURATION);
const USER_API = new UserServiceApi(API_CONFIGURATION);

function PageBase() {
  const [universes, setUniverses] = useState<Universe[]>([]);
  const [searchParams, setSearchParams] = useSearchParams();
  const [activeUniverse, setActiveUniverse] = useState<Universe>(null);
  const [currencySettings, setCurrencySettings] = useState<CurrencySettings>(null);
  const [username, setUsername] = useState<string>(null);
  const [user, setUser] = useState<PnPUser>(null);
  const [userPreferences, setUserPreferences] = useState<PnPUserPreference>(null);
  const [userPermissions, setUserPermissions] = useState<UserPermissions>({
    isAdmin: false,
    canCreateUniverses: false,
    canReadActiveUniverse: false,
    canWriteActiveUniverse: false,
    isActiveUniverseOwner: false
  });
  const [open, setOpen] = useState(true);

  const handleDrawerChange = () => {
    setOpen(!open);
  };

  async function fetchUniverses(): Promise<void> {
    const response = await UNIVERSE_API.getAllUniverses();
    setUniverses(response.data);
    const paramUniverse = response.data.find(u => u.name === searchParams.get("universe"));
    if (paramUniverse !== undefined) {
      setActiveUniverse(paramUniverse);
      return;
    }
    // If no universe is selected, select the previous selected universe of the user
    if (userPreferences) {
      const prefUniverse = response.data.find(u => u.name === userPreferences.lastSelectedUniverse);
      if (prefUniverse !== undefined) {
        setActiveUniverse(prefUniverse);
      }
    }
  }

  const refreshUser = () => {
    Promise.all([
      USER_API.getUser(username).then(response => response.data),
      USER_API.getUserPreferences(username).then(response => response.data)
    ]).then(([newUser, newPref]) => {
      setUser(newUser);
      setUserPreferences(newPref);
      if (newPref !== null && newPref.language !== null) {
        i18n.changeLanguage(newPref.language);
      }
    });
  };

  useEffect(() => {
    fetchUniverses();
    AUTHENTICATION_API.getUsername().then(response => {
      setUsername(response.data);
    });
  }, []);

  useEffect(() => {
    if (!activeUniverse) {
      return;
    }
    searchParams.set("universe", activeUniverse.name);
    setSearchParams(searchParams);
    SETTINGS_API.getCurrencySettings(activeUniverse.name).then(response => setCurrencySettings(response.data));
  }, [activeUniverse]);

  useEffect(() => {
    if (username === null) {
      return;
    }
    USER_API.getPermissions(username).then(response => {
      setUserPermissions(extractUserPermissions(response.data, activeUniverse));
    });
  }, [activeUniverse, username]);

  useEffect(() => {
    if (username === null) {
      return;
    }
    refreshUser();
  }, [username]);

  useEffect(() => {
    if (username === null || userPreferences === null || activeUniverse === null) {
      return;
    }
    if (userPreferences.lastSelectedUniverse === activeUniverse.name) {
      return;
    }
    USER_API.updateUserPreferences(username, {
      ...userPreferences,
      lastSelectedUniverse: activeUniverse.name
    });
  }, [activeUniverse, userPreferences]);

  return <ThemeProvider theme={THEME}>
    <Box sx={{ display: 'flex' }} data-testid="page-base">
      <CssBaseline />
      <NexusAppBar universes={universes} activeUniverse={activeUniverse} setActiveUniverse={setActiveUniverse} />
      <NexusSidebar collapsed={open} handleDrawerChange={handleDrawerChange} entries={generateSidebarEntries(userPermissions)} />
      <Box component="main" height="100vh" width="100%" display="flex" flexDirection="column" padding={2}>
        <Toolbar />
        <Box flex={1} overflow="auto">
          <Outlet context={{
            universes: universes,
            activeUniverse: activeUniverse,
            setActiveUniverse: setActiveUniverse,
            fetchUniverses: fetchUniverses,
            currencySettings: currencySettings,
            userPermissions: userPermissions,
            userPreferences: userPreferences,
            user: user,
            refreshUser: refreshUser
          }} />
        </Box>
      </Box>
    </Box>
  </ThemeProvider>;
}

function generateSidebarEntries(userPermissions: UserPermissions): MenuEntryProps[] {
  const { t } = useTranslation();

  const entries = [
    { id: "universe-menu", label: t("universe"), link: "/universe", icon: <TfiWorld /> },
    {
      id: "items-menu", label: t("items"), link: "/items", icon: <GiSwapBag />, subEntries: [
        { id: "weapons-menu", label: t("weapons"), link: "/weapons", icon: <GiAxeSword /> },
        { id: "shields-menu", label: t("shields"), link: "/shields", icon: <GiShield /> },
        { id: "armor-menu", label: t("armor"), link: "/armor", icon: <GiChestArmor /> },
        { id: "jewellery-menu", label: t("jewellery"), link: "/jewellery", icon: <GiRing /> },
        { id: "upgrades-menu", label: t("upgrades"), link: "/upgrades", icon: <GiMagicAxe /> },
        { id: "item-types-menu", label: t("item-types"), link: "/item-types", icon: <GiGearHammer /> },
        { id: "materials-menu", label: t("materials"), link: "/materials", icon: <GiClayBrick /> }
      ]
    },
    {
      id: "crafting-recipes-menu", label: t("crafting-recipes"), link: "/crafting-recipes", icon: <GiStoneCrafting />, subEntries: [
        { id: "upgrade-recipes-menu", label: t("upgrade-recipes"), link: "/upgrade-recipes", icon: <GiBurningBook /> }
      ]
    },
    {
      id: "characters-menu", label: t("characters"), link: "/characters", icon: <FaPersonRays />, subEntries: [
        { id: "spells-menu", label: t("spells"), link: "/spells", icon: <GiSpellBook /> },
        { id: "talents-menu", label: t("talents"), link: "/talents", icon: <GiSupersonicArrow /> },
        { id: "primary-attributes-menu", label: t("primary-attributes"), link: "/primary-attributes", icon: <GiMuscleUp /> },
        { id: "secondary-attributes-menu", label: t("secondary-attributes"), link: "/secondary-attributes", icon: <GiHeartInside /> }
      ]
    }
  ];

  if (userPermissions.isAdmin) {
    entries.push(
      {
        id: "admin-menu", label: t("admin"), link: "/admin", icon: <IoSettingsSharp />, subEntries: [
          { id: "users-menu", label: t("users"), link: "/users", icon: <HiUserCircle /> }
        ]
      }
    );
  }

  return entries;
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
