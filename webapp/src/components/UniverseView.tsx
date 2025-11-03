import {ReactNode} from 'react';
import {useUniverseContext, useUserContext} from './PageBase';
import {Button, Flex, Select, Text, Title} from '@mantine/core';
import {useTranslation} from 'react-i18next';
import {Link, useSearchParams} from 'react-router-dom';

/** A view which catches if no universe has been selected. */
export function UniverseView({children}: { children: ReactNode; }) {
    const {t} = useTranslation();
    const [searchParams] = useSearchParams();
    const {universes, activeUniverse, setActiveUniverse} = useUniverseContext();
    const {userPermissions} = useUserContext();

    if (activeUniverse) {
        return children;
    }

    return <Flex
        gap="md"
        justify="center"
        align="center"
        direction="column"
        wrap="wrap"
    >
        <Title>
            {t('universe:noUniverse')}
        </Title>
        {universes.length > 0 ?
            <>
                <Text>
                    {t('universe:pleaseSelectUniverse')}
                </Text>
                <Select
                    data={universes?.map(universe => {
                        return {value: universe.name, label: universe.displayName};
                    })}
                    value={activeUniverse?.name ?? null}
                    onChange={id => setActiveUniverse(universes.find(u => u.name === id))}
                    searchable
                    data-testid="universe-selector"
                />
            </>
            : userPermissions.canCreateUniverses ?
                <>
                    <Text>
                        {t('universe:createUniverseOrGetInvited')}
                    </Text>
                    <Button
                        component={Link}
                        to={{
                            pathname: '/universe-creation',
                            search: searchParams.toString()
                        }}
                    >
                        {t('universe:createUniverse')}
                    </Button>
                </>
                :
                <Text>
                    {t('universe:needToInvited')}
                </Text>
        }
    </Flex>;
}