import {Badge, Box, Card, Divider, Grid, Group, List, Modal, Progress, Stack, Text} from '@mantine/core';
import {useTranslation} from 'react-i18next';
import {currencyFormatter, diceFormatter, getRarityColor} from '../utils/Formatters';
import {useUniverseContext} from '../PageBase';
import {ArmorEquipment, ItemStack, JewelleryEquipment, ShieldEquipment, WeaponEquipment} from '../../api';
import {SomeItemStack} from '../Constants';
import {UpgradeHoverCard} from './UpgradeCard';

type ItemStackCombination =
    ItemStack
    & Partial<WeaponEquipment>
    & Partial<ShieldEquipment>
    & Partial<ArmorEquipment>
    & Partial<JewelleryEquipment>;

/** Visualizes a single item stack */
export function ItemStackCard({
    stack,
    onClick
}: {
    stack: SomeItemStack;
    onClick?: () => void;
}) {
    const {t} = useTranslation();
    const {currencySettings, itemSettings} = useUniverseContext();

    if (!stack) {
        return null;
    }

    const combItemStack = stack as ItemStackCombination;
    const baseItem = combItemStack.item;
    const itemType = baseItem['@type'];
    const stackPrefix = stack.stackSize !== 1 ? `${stack.stackSize}x ` : '';

    return (
        <Card
            shadow="sm"
            padding="lg"
            radius="md"
            withBorder
            w={400}
            onClick={onClick}
            style={{cursor: onClick ? 'pointer' : 'default'}}
        >
            {/* Header */}
            <Group justify="space-between" align="stretch">
                <Stack gap={0}>
                    <Group gap="xs">
                        <Text fw={700} size="xl">{stackPrefix + baseItem.name}</Text>
                    </Group>
                    <Text size="xs" c="dimmed" mb="xs">
                        {
                            (itemType.toLowerCase() !== 'armor' ? t(itemType.toLowerCase()) : t('enum:' + (stack as ArmorEquipment).armorSlot?.toLowerCase())) + ' • ' +
                            (baseItem.material ? ' ' + baseItem['material'].name + ' • ' : '') +
                            t('tier') + ' ' + baseItem.tier
                        }
                    </Text>

                    {/* Tags */}
                    {(baseItem.tags && baseItem.tags.length > 0) ? (
                        <Group gap={5} mb="md">
                            {baseItem.tags.map((tag) => (
                                <Badge key={tag} variant="outline" size="xs" color="gray">{tag}</Badge>
                            ))}
                        </Group>
                    ) : null}
                </Stack>
                <Stack justify="space-between">
                    <Badge color={getRarityColor(baseItem.rarity)} variant="light">
                        {t('enum:' + baseItem.rarity.toLowerCase())}
                    </Badge>
                    <UpgradeSlotBadge itemStack={combItemStack}/>
                </Stack>
            </Group>

            {/* Durability/Wear Bar (If applicable) */}
            {(itemSettings.wearFactor > 0 && combItemStack.relativeDurability !== undefined) ? (
                <Box mb="md">
                    <Text size="xs" c="dimmed" mb={2}>
                        {t('durability')}: {Math.round(combItemStack.relativeDurability * 100)}%
                    </Text>
                    <Progress
                        value={combItemStack.relativeDurability * 100}
                        size="xs"
                        color={combItemStack.relativeDurability < 0.2 ? 'red' : combItemStack.relativeDurability < 0.5 ? 'orange' : 'blue'}
                    />
                </Box>
            ) : null}

            <Divider variant="dashed" mb="sm"/>

            {/* Dynamic Stats Grid - Using values from Stack, falling back to Item */}
            <Grid grow mb="md" gutter="xs">
                <Stat
                    label={t('armor')}
                    current={combItemStack.armor}
                    max={combItemStack.maxArmor}
                />
                <Stat
                    label={t('protection')}
                    current={combItemStack.protection}
                    max={combItemStack.maxProtection}
                />
                <Stat label={t('weight')} current={combItemStack.weight ?? baseItem.weight}/>
                <Stat label={t('damage')} current={combItemStack.damage} max={combItemStack.maxDamage}/>
                <Stat label={t('dice')} current={diceFormatter(baseItem.dice)}/>
                <Stat label={t('hit')} current={combItemStack.hit ?? baseItem.hit}/>
                <Stat
                    label={t('initiative')}
                    current={combItemStack.initiative ?? baseItem.initiative}
                />
            </Grid>

            {/* Upgrades Section */}
            {(combItemStack.upgrades && combItemStack.upgrades.length > 0) ? (
                <Box mb="sm">
                    <Text size="xs" fw={700} c="dimmed" tt="uppercase" mb={4}>
                        {t('upgrades')}
                    </Text>
                    <Group gap={4}>
                        {combItemStack.upgrades.map((u, i) => (
                            <UpgradeHoverCard
                                key={i}
                                upgrade={u}
                                target={<Badge size="xs" variant="dot" color="blue">{u.name}</Badge>}
                            />
                        ))}
                    </Group>
                </Box>
            ) : null}

            {/* Effect & Description */}
            <Stack gap="xs">
                {(baseItem.effects && baseItem.effects.length > 0) ? (
                    <Box>
                        <Text size="xs" fw={700} c="dimmed">{t('upgrade:effects')}</Text>
                        <List size="sm">
                            {baseItem.effects.map((effect, index) => <List.Item key={index}>
                                {effect.description}
                            </List.Item>)}
                        </List>
                    </Box>
                ) : null}

                {baseItem.description ? (
                    <Box>
                        <Text size="xs" fw={700} c="dimmed">{t('description')}</Text>
                        <Text size="sm">{baseItem.description}</Text>
                    </Box>
                ) : null}
            </Stack>

            <Divider mb="sm" mt="sm"/>

            {/* Footer */}
            <Group justify="space-between" align="flex-end">
                <Stack gap={0}>
                    <Text size="xs" c="dimmed" fw={700}>{t('price')}</Text>
                    <Text fw={500} size="sm">
                        {currencyFormatter(currencySettings, (baseItem.vendorPrice ?? 0) * (stack.stackSize ?? 1))}
                    </Text>
                </Stack>
                {baseItem.requirement && (
                    <Text size="xs" c="red" fw={500}>
                        {t('requirement')}: {baseItem.requirement}
                    </Text>
                )}
            </Group>
        </Card>
    );
}


/**
 * An item stack card as modal.
 *  Opens if the item is not null.
 */
export function ItemStackCardModal({stack, onClose}: {
    stack: SomeItemStack;
    onClose: () => void;
}) {
    return <Modal
        opened={stack !== null}
        onClose={onClose}
        padding={0}
        withCloseButton={false}
        centered
        size="auto"
        radius="md"
    >
        {stack !== null ?
            <ItemStackCard stack={stack}/>
            : null
        }
    </Modal>;
}

function Stat({
    label,
    current,
    max
}: {
    label: string;
    current: string | number | undefined;
    max?: string | number
}) {
    if (current === undefined || current === '') {
        return null;
    }

    return (
        <Grid.Col span={6}>
            <Text size="xs" c="dimmed" fw={700} tt="uppercase">{label}</Text>
            <Group gap={4} align="baseline">
                <Text
                    size="sm"
                    fw={max && current < max ? 600 : 400}
                    c={max && current < max ? 'orange.8' : 'inherit'}
                >
                    {current}
                </Text>
                {(max && max !== current) ? <Text size="xs" c="dimmed">/ {max}</Text> : null}
            </Group>
        </Grid.Col>
    );
}

function UpgradeSlotBadge({itemStack}: { itemStack: ItemStackCombination }) {
    const {t} = useTranslation();
    const current = itemStack.remainingUpgradeSlots;
    const max = itemStack.upgradeSlots;

    return <Stack gap={1} mb="xs" mr="xs">
        <Text size="xs" c="dimmed" ta="right">
            {t('upgradeSlots')}
        </Text>
        <Group gap={4} align="baseline" justify="flex-end">
            <Text
                size="xs"
                fw={max && current < max ? 600 : 400}
                c={max && current < max ? 'orange.8' : 'inherit'}
                ta="right"
            >
                {current}
            </Text>
            {(max && max !== current) ? <Text size="xs" c="dimmed" ta="right">/ {max}</Text> : null}
        </Group>
    </Stack>;
}