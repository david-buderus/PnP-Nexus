import {Badge, Box, Card, Divider, Grid, Group, Modal, Stack, Text} from '@mantine/core';
import {useTranslation} from 'react-i18next';
import {Spell} from '../../api/model';
import {Stat} from '../items/ItemCard';
import {spellCastFormatter} from '../utils/Formatters';

/** Visualizes a single spell */
export function SpellCard({
    spell,
    onClick
}: {
    spell: Spell
    onClick?: () => void;
}) {
    const {t} = useTranslation();

    if (!spell) {
        return null;
    }

    const spellCast = spellCastFormatter(spell?.cast, t);

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
                    <Text fw={700} size="xl">{spell.name}</Text>
                    <Text size="xs" c="dimmed">
                        {
                            t('enum:' + spell.action.toLowerCase()) +
                            (spell.castingTypes.length > 0 ? ' • ' + spell.castingTypes.map(type => t('enum:' + type.toLowerCase())).join(', ') : '')
                        }
                    </Text>
                </Stack>
                <Badge variant="light">
                    {t('tier') + ' ' + spell.tier}
                </Badge>
            </Group>

            {/* Tags */}
            {(spell.tags && spell.tags.length > 0) ? (
                <Group gap={5} mb="md">
                    {spell.tags.map((tag) => (
                        <Badge key={tag} variant="outline" size="xs" color="gray">
                            {tag}
                        </Badge>
                    ))}
                </Group>
            ) : null}

            <Divider variant="dashed" mb="sm"/>

            {/* Effect & Countermeasures & Cast */}
            <Stack gap="xs">
                {spell.effect ? (
                    <Box>
                        <Text size="xs" fw={700} c="dimmed">{t('effect')}</Text>
                        <Text size="sm">{spell.effect}</Text>
                    </Box>
                ) : null}

                {spell.countermeasures ? (
                    <Box>
                        <Text size="xs" fw={700} c="dimmed">{t('spell:countermeasures')}</Text>
                        <Text size="sm">{spell.countermeasures}</Text>
                    </Box>
                ) : null}
                {spellCast.length > 0 ? (
                    <Box>
                        <Text size="xs" fw={700} c="dimmed">{t('spell:cast')}</Text>
                        <Text size="sm">{spellCast}</Text>
                    </Box>
                ) : null}
            </Stack>

            <Divider mb="sm" mt="sm"/>
            <Grid grow mb="md" gutter="xs">
                <Stat label={t('spell:castTime')} value={spell.castTime}/>
                <Stat label={t('spell:cooldown')} value={spell.cooldown}/>
                {spell.cost.map((cost, index) =>
                    <Stat key={index} label={cost.resource.name} value={cost.amount}/>
                )}
            </Grid>

            {spell.additionalCost ? <>
                <Divider variant="dashed" mb="sm"/>

                {/* Footer: Costs */}
                <Stack gap="xs">
                    <Text size="xs" c="dimmed" fw={700} tt="uppercase">{t('spell:additionalCost')}</Text>
                    <Text size="sm">{spell.additionalCost}</Text>
                </Stack>
            </> : null}
        </Card>
    );
}

/**
 * A spell card as modal.
 *  Opens if the spell is not null.
 */
export function SpellCardModal({spell, onClose}: {
    spell: Spell;
    onClose: () => void;
}) {
    return <Modal
        opened={spell !== null}
        onClose={onClose}
        padding={0}
        withCloseButton={false}
        centered
        size="auto"
        radius="md"
    >
        {spell !== null ?
            <SpellCard spell={spell}/>
            : null
        }
    </Modal>;
}