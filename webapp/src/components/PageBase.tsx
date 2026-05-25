import {
    AppShell,
    Burger,
    Group,
    Menu,
    NavLink,
    ScrollArea,
    Select,
    Stack,
    Text,
    Title,
    UnstyledButton
} from '@mantine/core';
import {useDisclosure} from '@mantine/hooks';
import {Link, Outlet, useOutletContext} from 'react-router-dom';
import {ReactElement, useEffect, useMemo} from 'react';
import i18n from '../i18n';
import {extractUserPermissions, UserPermissions} from './interfaces/UserPermissions';
import {useTranslation} from 'react-i18next';
import {
    GiAxeSword,
    GiBurningBook,
    GiChestArmor,
    GiClayBrick,
    GiMagicAxe,
    GiRing,
    GiShield,
    GiSpellBook,
    GiStoneCrafting,
    GiSupersonicArrow,
    GiSwapBag
} from 'react-icons/gi';
import {FaChevronDown, FaPersonRays} from 'react-icons/fa6';
import {TfiWorld} from 'react-icons/tfi';
import {IoSettingsSharp} from 'react-icons/io5';
import {HiUserCircle} from 'react-icons/hi2';
import {MdLogout} from 'react-icons/md';
import axios from 'axios';
import {PiPerson} from 'react-icons/pi';
import {ErrorBoundary} from './ErrorBoundary';
import {StringParam, useQueryParam, withDefault} from 'use-query-params';
import {BsPersonVcard} from 'react-icons/bs';
import {useGetAllUniverses} from '../api/universe-service/universe-service';
import {useGetUsername} from '../api/authentication-service/authentication-service';
import {
    useGetCharacterSettings,
    useGetCharacterSheetSettings,
    useGetCurrencySettings,
    useGetEquipmentSettings,
    useGetItemSettings
} from '../api/universe-settings-service/universe-settings-service';
import {
    getGetUserPreferencesQueryKey,
    useGetPermissions,
    useGetUser,
    useGetUserPreferences,
    useUpdateUserPreferences
} from '../api/user-service/user-service';
import {useQueryClient} from '@tanstack/react-query';
import {
    CharacterSettings,
    CharacterSheetSettings,
    CurrencySettings,
    EquipmentSettings,
    ItemSettings,
    PnPUser,
    PnPUserPreference,
    Universe
} from '../api/model';

type UniverseContext = {
    universes: Universe[];
    activeUniverse: Universe;
    setActiveUniverse: (activeUniverse: Universe) => void;
    currencySettings: CurrencySettings;
    itemSettings: ItemSettings;
    equipmentSettings: EquipmentSettings;
    characterSettings: CharacterSettings;
    sheetSettings: CharacterSheetSettings;
};

type UserContext = {
    userPermissions: UserPermissions,
    userPreferences: PnPUserPreference,
    user: PnPUser
};

/** Base of most pages in the webapp */
export function PageBase() {
    const queryClient = useQueryClient();
    const [universeQuery, setUniverseQuery] = useQueryParam('universe', withDefault(StringParam, null));

    const universes = useGetAllUniverses().data?.data ?? [];

    const username = useGetUsername().data?.data ?? null;
    const user = useGetUser(username, {query: {enabled: username !== null}}).data?.data ?? null;
    const userPreferences = useGetUserPreferences(username, {
        query: {enabled: username !== null},
    }).data?.data;
    useEffect(() => {
        if (userPreferences && userPreferences.language !== null) {
            i18n.changeLanguage(userPreferences.language);
        }
    }, [userPreferences, i18n]);

    const activeUniverse = useMemo(() => {
        if (universes.length === 0) {
            return null;
        }

        const paramUniverse = universes.find(u => u.id === universeQuery);
        if (paramUniverse) {
            return paramUniverse;
        }

        if (userPreferences) {
            const prefUniverse = universes.find(u => u.id === userPreferences.lastSelectedUniverse);
            if (prefUniverse) {
                return prefUniverse;
            }
        }

        return null;
    }, [universes, universeQuery, userPreferences]);

    useEffect(() => {
        if (!activeUniverse) {
            return;
        }
        if (universeQuery !== activeUniverse.id) {
            setUniverseQuery(activeUniverse.id);
        }
    }, [activeUniverse, universeQuery, setUniverseQuery]);

    const {data: permissionsResponse} = useGetPermissions(username, {query: {enabled: username !== null}});
    const userPermissions = useMemo(() => {
        if (!permissionsResponse?.data) {
            return {
                isAdmin: false,
                canCreateUniverses: false,
                canReadActiveUniverse: false,
                canWriteActiveUniverse: false,
                isActiveUniverseOwner: false
            };
        }
        return extractUserPermissions(permissionsResponse.data, activeUniverse);
    }, [permissionsResponse, activeUniverse]);

    const currencySettings = useGetCurrencySettings(activeUniverse?.id, {query: {enabled: activeUniverse !== null}}).data?.data ?? null;
    const itemSettings = useGetItemSettings(activeUniverse?.id, {query: {enabled: activeUniverse !== null}}).data?.data ?? null;
    const equipmentSettings = useGetEquipmentSettings(activeUniverse?.id, {query: {enabled: activeUniverse !== null}}).data?.data ?? null;
    const characterSettings = useGetCharacterSettings(activeUniverse?.id, {query: {enabled: activeUniverse !== null}}).data?.data ?? null;
    const sheetSettings = useGetCharacterSheetSettings(activeUniverse?.id, {query: {enabled: activeUniverse !== null}}).data?.data ?? null;

    const {mutate: updateUserPreferences} = useUpdateUserPreferences({
        mutation: {
            onSuccess: () => queryClient.invalidateQueries({
                queryKey: getGetUserPreferencesQueryKey(username),
            })
        }
    });
    useEffect(() => {
        if (!username || !userPreferences || !activeUniverse) {
            return;
        }
        if (userPreferences.lastSelectedUniverse === activeUniverse.id) {
            return;
        }

        updateUserPreferences({
            username: username,
            data: {
                ...userPreferences,
                lastSelectedUniverse: activeUniverse.id
            }
        });
    }, [activeUniverse, userPreferences, username, updateUserPreferences]);

    const [opened, {toggle}] = useDisclosure();

    const searchParams = new URLSearchParams();
    if (universeQuery) {
        searchParams.set('universe', universeQuery);
    }

    return (
        <AppShell
            header={{height: 70}}
            navbar={{width: 300, breakpoint: 'sm', collapsed: {mobile: !opened}}}
            padding="md"
            data-testid="page-base"
        >
            <AppShell.Header>
                <Group justify="space-between" align="center">
                    <Group align="center">
                        <Burger opened={opened} onClick={toggle} hiddenFrom="sm" size="sm"/>
                        <UnstyledButton
                            component={Link}
                            to={{
                                pathname: '/',
                                search: searchParams.toString()
                            }}
                            data-testid="main-menu"
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
                        setActiveUniverse={u => setUniverseQuery(u?.id)}
                        searchParams={searchParams.toString()}
                    />
                </Group>
            </AppShell.Header>
            <AppShell.Navbar p="md">
                <ScrollArea>
                    <Stack>
                        {generateSidebarEntries(userPermissions).map((item) =>
                            <NavbarEntry
                                key={item.id}
                                searchParams={searchParams.toString()}
                                {...item}
                            />
                        )}
                    </Stack>
                </ScrollArea>
            </AppShell.Navbar>
            <AppShell.Main>
                <ErrorBoundary>
                    <Outlet context={{
                        universes: universes,
                        activeUniverse: activeUniverse,
                        setActiveUniverse: (u: Universe) => setUniverseQuery(u?.id ?? null),
                        currencySettings: currencySettings,
                        itemSettings: itemSettings,
                        equipmentSettings: equipmentSettings,
                        characterSettings: characterSettings,
                        sheetSettings: sheetSettings,
                        userPermissions: userPermissions,
                        userPreferences: userPreferences,
                        user: user
                    }}/>
                </ErrorBoundary>
            </AppShell.Main>
        </AppShell>
    );
}

/** Props for a navbar entry */
interface NavbarEntryProps {
    /** The id of the entry */
    id: string;
    /** The label shown */
    label: string;
    /** The icon show */
    icon: ReactElement;
    /** The link where to navigate to */
    link: string;
    /** Possible subentries */
    subEntries?: {
        id: string;
        label: string;
        icon: ReactElement;
        link: string;
    }[];
}

function NavbarEntry({id, label, icon, link, subEntries, searchParams}: NavbarEntryProps & { searchParams: string; }) {
    if (!subEntries) {
        return <NavLink
            component={Link}
            key={id}
            data-testid={id}
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
        data-testid={id}
        label={label}
        leftSection={icon}
    >
        <NavLink
            component={Link}
            key={id + '-inner'}
            data-testid={id + '-inner'}
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
                data-testid={subItem.id}
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
    const {t} = useTranslation();

    const entries = [
        {
            id: 'universe-menu', label: t('universe'), link: '/universe', icon: <TfiWorld/>, subEntries: [
                {id: 'species-menu', label: t('species'), link: '/species', icon: <PiPerson/>},
            ]
        },
        {
            id: 'items-menu', label: t('items'), link: '/items', icon: <GiSwapBag/>, subEntries: [
                {id: 'weapons-menu', label: t('weapons'), link: '/weapons', icon: <GiAxeSword/>},
                {id: 'shields-menu', label: t('shields'), link: '/shields', icon: <GiShield/>},
                {id: 'armor-menu', label: t('armor'), link: '/armor', icon: <GiChestArmor/>},
                {id: 'jewellery-menu', label: t('jewellery'), link: '/jewellery', icon: <GiRing/>},
                {id: 'upgrades-menu', label: t('upgrades'), link: '/upgrades', icon: <GiMagicAxe/>},
                {id: 'materials-menu', label: t('materials'), link: '/materials', icon: <GiClayBrick/>}
            ]
        },
        {
            id: 'crafting-recipes-menu',
            label: t('crafting-recipes'),
            link: '/crafting-recipes',
            icon: <GiStoneCrafting/>,
            subEntries: [
                {
                    id: 'upgrade-recipes-menu',
                    label: t('upgrade-recipes'),
                    link: '/upgrade-recipes',
                    icon: <GiBurningBook/>
                }
            ]
        },
        {
            id: 'characters-menu', label: t('characters'), link: '/characters', icon: <FaPersonRays/>, subEntries: [
                {id: 'spells-menu', label: t('spells'), link: '/spells', icon: <GiSpellBook/>},
                {id: 'talents-menu', label: t('talents'), link: '/talents', icon: <GiSupersonicArrow/>},
                {
                    id: 'editor-menu',
                    label: t('sheetEditor:character-sheets'),
                    link: '/characters-editor',
                    icon: <BsPersonVcard/>
                }
            ]
        }
    ];

    if (userPermissions.isAdmin) {
        entries.push(
            {
                id: 'admin-menu', label: t('admin'), link: '/admin', icon: <IoSettingsSharp/>, subEntries: [
                    {id: 'users-menu', label: t('users'), link: '/users', icon: <HiUserCircle/>}
                ]
            }
        );
    }

    return entries;
}

function UserMenu({user, activeUniverse, setActiveUniverse, universes, searchParams}: {
    user: PnPUser;
    activeUniverse: Universe;
    setActiveUniverse: (universe: Universe) => void;
    universes: Universe[];
    searchParams: string;
}) {
    const {t} = useTranslation();

    return <Menu
        width={260}
        position="bottom-end"
        transitionProps={{transition: 'pop-top-right'}}
        withinPortal
    >
        <Menu.Target>
            <UnstyledButton
                style={{
                    padding: 'var(--mantine-spacing-md)',
                    color: 'var(--mantine-color-text)',
                    borderRadius: 'var(--mantine-radius-sm)',
                }}
                data-testid="menu-appbar"
            >
                <Group gap={7}>
                    <Text fw={500} size="sm" lh={1} mr={3}>
                        {user?.displayName}
                    </Text>
                    <FaChevronDown size={12}/>
                </Group>
            </UnstyledButton>
        </Menu.Target>
        <Menu.Dropdown>
            <Menu.Label>{t('universe')}</Menu.Label>
            <Menu.Item closeMenuOnClick={false}>
                <Select
                    data={universes?.map(universe => ({value: universe.id, label: universe.displayName})) ?? []}
                    value={activeUniverse?.id}
                    onChange={id => setActiveUniverse(universes.find(u => u.id === id))}
                    placeholder={t('universe:noUniverse') + '...'}
                    disabled={universes.length === 0}
                    variant="unstyled"
                    searchable
                />
            </Menu.Item>
            <Menu.Label>{t('preferences')}</Menu.Label>
            <Menu.Item
                data-testid="user-menu"
                component={Link}
                to={{
                    pathname: '/user',
                    search: searchParams.toString()
                }}
            >
                {t('profile')}
            </Menu.Item>
            <Menu.Item
                data-testid="preferences-menu"
                component={Link}
                to={{
                    pathname: '/preferences',
                    search: searchParams.toString()
                }}
            >
                {t('preferences')}
            </Menu.Item>
            <Menu.Divider/>
            <Menu.Item
                leftSection={<MdLogout size={16}/>}
                onClick={() => {
                    axios.post('/logout');
                    window.location.reload();
                }}
            >
                {t('logout')}
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