import {Badge, Box, Card, Divider, Grid, Group, List, Modal, Stack, Text} from '@mantine/core';
import {useTranslation} from 'react-i18next';
import {currencyFormatter, diceFormatter, getRarityColor} from '../utils/Formatters';
import {useUniverseContext} from '../PageBase';
import {ItemCombination} from '../../pages/database/items/items'; // Adjust path

/** Visualizes a single item */
export function ItemCard({
    item,
    onClick
}: {
    item: ItemCombination
    onClick?: () => void;
}) {
    const {t} = useTranslation();
    const {currencySettings} = useUniverseContext();
    const itemType = item['@type'];


    if (!item) {
        return null;
    }

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
            {/* Header: Name, Rarity and Tier */}
            <Group justify="space-between" mb="xs" align="flex-start">
                <Stack gap={0}>
                    <Text fw={700} size="xl">{item.name || t('item:unnamed')}</Text>
                    <Text size="xs" c="dimmed">
                        {
                            (itemType.toLowerCase() !== 'armor' ? t(itemType.toLowerCase()) : t('enum:' + item.armorSlot.toLowerCase())) + ' • ' +
                            (item.material ? ' ' + item.material.name + ' • ' : '') +
                            t('tier') + ' ' + item.tier
                        }
                    </Text>
                </Stack>
                <Badge color={getRarityColor(item.rarity)} variant="light">
                    {t('enum:' + item.rarity.toLowerCase())}
                </Badge>
            </Group>

            {/* Tags */}
            {(item.tags && item.tags.length > 0) ? (
                <Group gap={5} mb="md">
                    {item.tags.map((tag) => (
                        <Badge key={tag} variant="outline" size="xs" color="gray">
                            {tag}
                        </Badge>
                    ))}
                </Group>
            ) : null}

            <Divider variant="dashed" mb="sm"/>

            {/* Primary Stats Grid */}
            <Grid grow mb="md" gutter="xs">
                <Stat label={t('armor')} value={item.armor}/>
                <Stat label={t('protection')} value={item.protection}/>
                <Stat label={t('weight')} value={item.weight}/>
                <Stat label={t('damage')} value={item.damage}/>
                <Stat label={t('dice')} value={diceFormatter(item.dice)}/>
                <Stat label={t('hit')} value={item.hit}/>
                <Stat label={t('initiative')} value={item.initiative}/>
                <Stat label={t('upgradeSlots')} value={item.upgradeSlots}/>
            </Grid>

            {/* Effect & Description */}
            <Stack gap="xs">
                {(item.effects && item.effects.length > 0) ? (
                    <Box>
                        <Text size="xs" fw={700} c="dimmed">{t('upgrade:effects')}</Text>
                        <List size="sm">
                            {item.effects.map((effect, index) => <List.Item key={index}>
                                {effect.description}
                            </List.Item>)}
                        </List>
                    </Box>
                ) : null}

                {item.description ? (
                    <Box>
                        <Text size="xs" fw={700} c="dimmed">{t('description')}</Text>
                        <Text size="sm">{item.description}</Text>
                    </Box>
                ) : null}
            </Stack>

            <Divider mb="sm" mt="sm"/>

            {/* Footer: Price and Requirements */}
            <Group justify="space-between" align="flex-end">
                <Stack gap={0}>
                    <Text size="xs" c="dimmed" fw={700}>{t('price')}</Text>
                    <Text fw={500} size="sm">
                        {currencyFormatter(currencySettings, item.vendorPrice)}
                    </Text>
                </Stack>
                {item.requirement && (
                    <Text size="xs" c="red" fw={500}>
                        {t('requirement')}: {item.requirement}
                    </Text>
                )}
            </Group>
        </Card>
    );
}

/**
 * An item card as modal.
 *  Opens if the item is not null.
 */
export function ItemCardModal({item, onClose}: {
    item: ItemCombination;
    onClose: () => void;
}) {
    return <Modal
        opened={item !== null}
        onClose={onClose}
        padding={0}
        withCloseButton={false}
        centered
        size="auto"
        radius="md"
    >
        {item !== null ?
            <ItemCard item={item}/>
            : null
        }
    </Modal>;
}

/**
 * Displays a stat.
 * Needs to be in a Grid.
 */
export function Stat({label, value}: { label: string; value: string | number | undefined }) {
    if (value === undefined || value === '') {
        return null;
    }
    return (
        <Grid.Col span={6}>
            <Text size="xs" c="dimmed" fw={700} tt="uppercase">{label}</Text>
            <Text size="sm">{value}</Text>
        </Grid.Col>
    );
}