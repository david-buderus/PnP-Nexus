import {Badge, Box, Card, Divider, Group, List, Modal, Stack, Text} from '@mantine/core';
import {currencyFormatter} from '../utils/Formatters';
import {useUniverseContext} from '../PageBase';
import {TagRequirement, Upgrade} from '../../api';
import {useTranslation} from 'react-i18next';

type UpgradeCardProps = {
    upgrade: Upgrade;
    onClick?: () => void;
    enoughSlots?: boolean;
}

/** Visualizes a single item upgrade */
export function UpgradeCard({upgrade, onClick, enoughSlots = true}: UpgradeCardProps) {
    const {t} = useTranslation();
    const {currencySettings} = useUniverseContext();

    if (!upgrade) {
        return null;
    }

    const formattedTags = formatTagRequirements(upgrade.tagRequirement);

    return (
        <Card
            shadow="sm"
            padding="lg"
            radius="md"
            withBorder
            w={400}
            onClick={onClick}
            style={{
                cursor: onClick ? 'pointer' : 'default',
            }}
        >
            {/* Header: Name and Restriction */}
            <Group justify="space-between" mb="xs" align="flex-start">
                <Stack gap={0}>
                    <Text fw={700} size="xl">{upgrade.name || t('item:unnamed')}</Text>
                    <Text size="xs" c="dimmed">
                        {t('upgrade:restriction')}: {t('enum:' + upgrade.restriction.toLowerCase())}
                    </Text>
                </Stack>
                <Badge color={enoughSlots ? 'blue' : 'red'} variant="light" size="md">
                    {upgrade.slots} {upgrade.slots === 1 ? t('upgrade:slot') : t('upgrade:slots')}
                </Badge>
            </Group>

            {/* Tag Requirements (if any exist) */}
            {formattedTags ? (
                <Group gap={5} mb="md">
                    <Text size="xs" c="dimmed" fw={500}>{t('upgrade:tagRequirement')}:</Text>
                    <Badge variant="outline" size="xs">
                        {formattedTags}
                    </Badge>
                </Group>
            ) : null}

            <Divider variant="dashed" mb="sm"/>

            {/* Effects List */}
            <Stack gap="xs" style={{flexGrow: 1}}>
                {upgrade.effects && upgrade.effects.length > 0 ? (
                    <Box>
                        <Text size="xs" fw={700} c="dimmed">{t('upgrade:effects')}</Text>
                        <List size="sm">
                            {upgrade.effects.map((effect, index) => (
                                <List.Item key={index}>
                                    {effect.description}
                                </List.Item>
                            ))}
                        </List>
                    </Box>
                ) : (
                    <Text size="sm" c="dimmed" fs="italic">
                        {t('upgrade:no_effects')}
                    </Text>
                )}
            </Stack>

            <Divider mb="sm" mt="sm"/>

            {/* Footer: Price */}
            <Group justify="space-between" align="flex-end">
                <Stack gap={0}>
                    <Text size="xs" c="dimmed" fw={700}>{t('price')}</Text>
                    <Text fw={500} size="sm">
                        {currencyFormatter(currencySettings, upgrade.vendorPrice)}
                    </Text>
                </Stack>
            </Group>
        </Card>
    );
}

/**
 * An item card as modal.
 *  Opens if the item is not null.
 */
export function UpgradeCardModal({upgrade, onClose}: {
    upgrade: Upgrade;
    onClose: () => void;
}) {
    return <Modal
        opened={upgrade !== null}
        onClose={onClose}
        padding={0}
        withCloseButton={false}
        centered
        size="auto"
        radius="md"
    >
        {upgrade !== null ?
            <UpgradeCard upgrade={upgrade}/>
            : null
        }
    </Modal>;
}

/** Helper to format the nested TagRequirement structure: [[A, B], [C]] -> "(A AND B) OR (C)" */
function formatTagRequirements(requirement?: TagRequirement): string | null {
    if (!requirement?.tagRequirements || requirement.tagRequirements.length === 0) {
        return null;
    }

    return requirement.tagRequirements
        .map(group => group.join(' & '))
        .filter(groupStr => groupStr.length > 0)
        .join(' | ');
}