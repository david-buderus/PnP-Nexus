import { useTranslation } from "react-i18next";
import { getUniverseContext, getUserContext } from "../components/PageBase";
import { Link, useNavigate, useSearchParams } from "react-router-dom";
import { Card, Grid, Stack, Text } from "@mantine/core";
import { FaPlus } from "react-icons/fa6";


export default function Home() {
    const { t } = useTranslation();
    const [searchParams] = useSearchParams();
    const { universes, setActiveUniverse, activeUniverse } = getUniverseContext();
    const { userPermissions } = getUserContext();
    const navigate = useNavigate();

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