import { AppShell, Burger, Group, Menu, NavLink, ScrollArea, Text, Stack, UnstyledButton, Title, Popover, Select } from "@mantine/core";
import { useDisclosure } from "@mantine/hooks";
import { UserPermissions } from "../../src old/components/interfaces/UserPermissions";
import { Link, Outlet, useOutletContext, useSearchParams } from "react-router-dom";
import { ReactElement, useEffect, useState } from "react";
import { Universe, CurrencySettings, ItemSettings, PnPUser, PnPUserPreference, UniverseServiceApi, UniverseSettingsServiceApi, AuthenticationServiceApi, UserServiceApi, CharacterSettings } from "../api";
import { API_CONFIGURATION } from "./Constants";
import i18n from "../i18n";
import { extractUserPermissions } from "./interfaces/UserPermissions";
import { useTranslation } from "react-i18next";
import { GiAxeSword, GiBurningBook, GiChestArmor, GiClayBrick, GiHeartInside, GiMagicAxe, GiMuscleUp, GiRing, GiShield, GiSpellBook, GiStoneCrafting, GiSupersonicArrow, GiSwapBag } from 'react-icons/gi';
import { FaChevronDown, FaPersonRays } from "react-icons/fa6";
import { TfiWorld } from "react-icons/tfi";
import { IoSettingsSharp } from "react-icons/io5";
import { HiUserCircle } from "react-icons/hi2";
import { MdLogout } from "react-icons/md";
import axios from "axios";

type UniverseContext = {
    universes: Universe[];
    activeUniverse: Universe;
    setActiveUniverse: (activeUniverse: Universe) => void;
    fetchUniverses: () => Promise<void>;
    currencySettings: CurrencySettings;
    itemSettings: ItemSettings;
    characterSettings: CharacterSettings;
    refreshSettings: () => void;
};

type UserContext = {
    userPermissions: UserPermissions,
    userPreferences: PnPUserPreference,
    user: PnPUser,
    refreshUser: () => void;
};

const UNIVERSE_API = new UniverseServiceApi(API_CONFIGURATION);
const SETTINGS_API = new UniverseSettingsServiceApi(API_CONFIGURATION);
const AUTHENTICATION_API = new AuthenticationServiceApi(API_CONFIGURATION);
const USER_API = new UserServiceApi(API_CONFIGURATION);

export function PageBase() {
    const [universes, setUniverses] = useState<Universe[]>([]);
    const [searchParams, setSearchParams] = useSearchParams();
    const [activeUniverse, setActiveUniverse] = useState<Universe>(null);
    const [currencySettings, setCurrencySettings] = useState<CurrencySettings>(null);
    const [itemSettings, setItemSettings] = useState<ItemSettings>(null);
    const [characterSettings, setCharacterSettings] = useState<CharacterSettings>(null);
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
    const [opened, { toggle }] = useDisclosure();

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

    function refreshSettings() {
        SETTINGS_API.getCurrencySettings(activeUniverse.name).then(response => setCurrencySettings(response.data));
        SETTINGS_API.getItemSettings(activeUniverse.name).then(response => setItemSettings(response.data));
        SETTINGS_API.getCharacterSettings(activeUniverse.name).then(response => setCharacterSettings(response.data));
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
        SETTINGS_API.getItemSettings(activeUniverse.name).then(response => setItemSettings(response.data));
        SETTINGS_API.getCharacterSettings(activeUniverse.name).then(response => setCharacterSettings(response.data));
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

    return (
        <AppShell
            header={{ height: 70 }}
            navbar={{ width: 300, breakpoint: 'sm', collapsed: { mobile: !opened } }}
            padding="md"
        >
            <AppShell.Header>
                <Group justify="space-between" align="center">
                    <Group align="center">
                        <Burger opened={opened} onClick={toggle} hiddenFrom="sm" size="sm" />
                        <UnstyledButton
                            component={Link}
                            to={{
                                pathname: "/",
                                search: searchParams.toString()
                            }}
                        >
                            <Title
                                order={1}
                                p="sm"
                            >
                                P&P Nexus
                            </Title>
                        </UnstyledButton>
                    </Group>
                    <UserMenu
                        user={user}
                        universes={universes}
                        activeUniverse={activeUniverse}
                        setActiveUniverse={setActiveUniverse}
                        searchParams={searchParams.toString()}
                    />
                </Group>
            </AppShell.Header>
            <AppShell.Navbar p="md">
                <ScrollArea >
                    <Stack>
                        {generateSidebarEntries(userPermissions).map((item) =>
                            <NavbarEntry
                                searchParams={searchParams.toString()}
                                {...item}
                            />
                        )}
                    </Stack>
                </ScrollArea>
            </AppShell.Navbar>
            <AppShell.Main>
                <Outlet context={{
                    universes: universes,
                    activeUniverse: activeUniverse,
                    setActiveUniverse: setActiveUniverse,
                    fetchUniverses: fetchUniverses,
                    currencySettings: currencySettings,
                    itemSettings: itemSettings,
                    characterSettings: characterSettings,
                    userPermissions: userPermissions,
                    userPreferences: userPreferences,
                    user: user,
                    refreshUser: refreshUser,
                    refreshSettings: refreshSettings
                }} />
            </AppShell.Main>
        </AppShell>
    );
}

interface NavbarEntryProps {
    id: string;
    label: string;
    icon: ReactElement;
    link: string;
    subEntries?: {
        id: string;
        label: string;
        icon: ReactElement;
        link: string;
    }[];
}

function NavbarEntry({ id, label, icon, link, subEntries, searchParams }: NavbarEntryProps & { searchParams: string; }) {
    if (!subEntries) {
        return <NavLink
            component={Link}
            key={id}
            label={label}
            leftSection={icon}
            to={{
                pathname: link,
                search: searchParams
            }}
        />;
    }

    return <NavLink
        key={id}
        label={label}
        leftSection={icon}
    >
        <NavLink
            component={Link}
            key={id + "-inner"}
            label={label}
            leftSection={icon}
            to={{
                pathname: link,
                search: searchParams
            }}
        />
        {subEntries?.map((subItem) =>
            <NavLink
                component={Link}
                key={subItem.id}
                label={subItem.label}
                leftSection={subItem.icon}
                to={{
                    pathname: subItem.link,
                    search: searchParams
                }}
            />
        )}
    </NavLink>;
}

function generateSidebarEntries(userPermissions: UserPermissions): NavbarEntryProps[] {
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
                { id: "talents-menu", label: t("talents"), link: "/talents", icon: <GiSupersonicArrow /> }
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

function UserMenu({ user, activeUniverse, setActiveUniverse, universes, searchParams }: {
    user: PnPUser;
    activeUniverse: Universe;
    setActiveUniverse: (universe: Universe) => void;
    universes: Universe[];
    searchParams: string;
}) {
    const { t } = useTranslation();

    return <Menu
        width={260}
        position="bottom-end"
        transitionProps={{ transition: 'pop-top-right' }}
        withinPortal
    >
        <Menu.Target>
            <UnstyledButton
                style={{
                    padding: 'var(--mantine-spacing-md)',
                    color: 'var(--mantine-color-text)',
                    borderRadius: 'var(--mantine-radius-sm)',
                }}
            >
                <Group gap={7}>
                    <Text fw={500} size="sm" lh={1} mr={3}>
                        {user?.displayName}
                    </Text>
                    <FaChevronDown size={12} />
                </Group>
            </UnstyledButton>
        </Menu.Target>
        <Menu.Dropdown>
            <Menu.Label>{t("universe")}</Menu.Label>
            <Menu.Item closeMenuOnClick={false}>
                <Select
                    data={universes?.map(universe => { return { value: universe.name, label: universe.displayName }; })}
                    value={activeUniverse?.name}
                    onChange={id => setActiveUniverse(universes.find(u => u.name === id))}
                    placeholder={t("universe:noUniverse") + "..."}
                    disabled={universes.length === 0}
                    variant="unstyled"
                    searchable
                />
            </Menu.Item>
            <Menu.Label>{t("preferences")}</Menu.Label>
            <Menu.Item
                component={Link}
                to={{
                    pathname: "/user",
                    search: searchParams.toString()
                }}
            >
                {t("profile")}
            </Menu.Item>
            <Menu.Item
                component={Link}
                to={{
                    pathname: "/preferences",
                    search: searchParams.toString()
                }}
            >
                {t("preferences")}
            </Menu.Item>
            <Menu.Divider />
            <Menu.Item
                leftSection={<MdLogout size={16} />}
                onClick={() => {
                    axios.post("/logout");
                    window.location.reload();
                }}
            >
                {t("logout")}
            </Menu.Item>

        </Menu.Dropdown>
    </Menu>;
}

/** Returns context over the currently avaible universes. */
export function useUniverseContext() {
    return useOutletContext<UniverseContext>();
}

/** Returns context over the currently logged in user. */
export function useUserContext() {
    return useOutletContext<UserContext>();
}

export default PageBase;