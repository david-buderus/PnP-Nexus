import { useTranslation } from "react-i18next";
import { useUniverseContext, useUserContext } from "../components/PageBase";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import { Card, Flex, Grid, Stack, Text, Title } from "@mantine/core";
import { FaPlus } from "react-icons/fa6";
import { useEffect } from "react";


export default function Home() {
    const { t } = useTranslation();
    const [searchParams] = useSearchParams();
    const { universes, fetchUniverses, setActiveUniverse, activeUniverse } = useUniverseContext();
    const { userPermissions } = useUserContext();
    const navigate = useNavigate();

    useEffect(() => {
        fetchUniverses();
    }, []);

    if (universes.length === 0 && !userPermissions.canCreateUniverses) {
        return <Flex
            gap="md"
            justify="center"
            align="center"
            direction="column"
            wrap="wrap"
        >
            <Title>
                {t("universe:noUniverse")}
            </Title>
            <Text>
                {t("universe:needToInvited")}
            </Text>
        </Flex>;
    }

    return <Grid>
        {universes.map(u => <Grid.Col span={2}>
            <Card
                shadow="sm"
                padding="xl"
                onClick={() => {
                    setActiveUniverse(u);
                    navigate("/universe");
                }}
                key={u.name}
                withBorder={activeUniverse?.name === u.name}
            >
                <Text fw={500} size="lg" mt="md">
                    {u.displayName}
                </Text>

                <Text mt="xs" c="dimmed" size="sm">
                    {u.shortDescription}
                </Text>
            </Card>
        </Grid.Col>)}

        {userPermissions.canCreateUniverses &&
            <Grid.Col span={2}>
                <Card
                    shadow="sm"
                    padding="xl"
                    component={Link}
                    to={{
                        pathname: "/universe-creation",
                        search: searchParams.toString()
                    }}
                >
                    <Stack align="center">
                        <Text fw={500} size="lg" mt="md" ta="center">
                            {t("universe:createUniverse")}
                        </Text>
                        <FaPlus size={30} />
                    </Stack>
                </Card>
            </Grid.Col>
        }
    </Grid>;
}