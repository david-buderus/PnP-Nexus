import {ActionIcon, Card, Group, Stack} from '@mantine/core';
import {FaChevronDown, FaChevronUp, FaRegTrashCan} from 'react-icons/fa6';
import React, {ReactNode, useMemo} from 'react';
import {toIdMap} from '../../../utils/Utils';
import {useTranslation} from 'react-i18next';

/** Interface for needed attributes */
interface Orderable {
    /** The id of the object */
    id?: string;
    /** The name of the object */
    name: string;
}

/** Component to modify the order of a list of objects */
export function OrderModifier<T extends Orderable>({
    currentOrder, setOrder, fullList
}: {
    currentOrder: string[]
    setOrder: (s: string[]) => void;
    fullList: T[];
}) {
    const order = useMemo(() => {
        const existingIds = new Set(fullList.map(t => t.id));
        const ordered = (currentOrder ?? []).filter(id => existingIds.has(id));
        const missing = fullList
            .map(t => t.id)
            .filter(id => !ordered.includes(id));

        return [...ordered, ...missing];
    }, [currentOrder, fullList]);
    const mappedList = toIdMap(fullList);

    function moveUp(index: number) {
        const copy = [...order];
        const item = copy.splice(index, 1)[0];
        copy.splice(index - 1, 0, item);
        setOrder(copy);
    }

    function moveDown(index: number) {
        const copy = [...order];
        const item = copy.splice(index, 1)[0];
        copy.splice(index + 1, 0, item);
        setOrder(copy);
    }

    return <Stack gap={1}>
        {order.map((id: string, index: number) => (
            <Card key={id} shadow="sm">
                <Group wrap="nowrap" justify="space-between">
                    {mappedList[id].name}
                    <Group wrap="nowrap" gap={0}>
                        <ActionIcon
                            variant="outline"
                            disabled={index === 0}
                            onClick={() => moveUp(index)}
                        >
                            <FaChevronUp/>
                        </ActionIcon>
                        <ActionIcon
                            variant="outline"
                            disabled={index === order.length - 1}
                            onClick={() => moveDown(index)}
                        >
                            <FaChevronDown/>
                        </ActionIcon>
                    </Group>
                </Group>
            </Card>
        ))}
    </Stack>;
}

/** Component to modify the order of a list of objects */
export function AddableOrderModifier<T extends Orderable>({
    order, setOrder, fullList, addDialog
}: {
    order: string[]
    setOrder: (s: string[]) => void;
    fullList: T[];
    addDialog: (add: (object: T | T[]) => void) => ReactNode;
}) {
    const {t} = useTranslation();
    const mappedList = toIdMap(fullList);

    function add(object: T | T[]) {
        const copy = [...order];
        if (Array.isArray(object)) {
            copy.push(...object.map(o => o.id));
        } else {
            copy.push(object?.id ?? null);
        }
        setOrder(copy);
    }

    function remove(index: number) {
        const copy = [...order];
        copy.splice(index, 1);
        setOrder(copy);
    }

    function moveUp(index: number) {
        const copy = [...order];
        const item = copy.splice(index, 1)[0];
        copy.splice(index - 1, 0, item);
        setOrder(copy);
    }

    function moveDown(index: number) {
        const copy = [...order];
        const item = copy.splice(index, 1)[0];
        copy.splice(index + 1, 0, item);
        setOrder(copy);
    }

    return <Stack gap={1}>
        {order.map((id: string, index: number) => (
            <Card key={id ? id : ('empty-row-' + index)} shadow="sm">
                <Group wrap="nowrap" justify="space-between">
                    {mappedList[id]?.name ?? t('sheetEditor:emptyRow')}
                    <Group wrap="nowrap" gap={0}>
                        <ActionIcon
                            variant="outline"
                            color="red"
                            onClick={() => remove(index)}
                        >
                            <FaRegTrashCan/>
                        </ActionIcon>
                        <ActionIcon
                            variant="outline"
                            disabled={index === 0}
                            onClick={() => moveUp(index)}
                        >
                            <FaChevronUp/>
                        </ActionIcon>
                        <ActionIcon
                            variant="outline"
                            disabled={index === order.length - 1}
                            onClick={() => moveDown(index)}
                        >
                            <FaChevronDown/>
                        </ActionIcon>
                    </Group>
                </Group>
            </Card>
        ))}
        {addDialog(add)}
        <Card
            style={{cursor: 'pointer'}}
            shadow="sm"
            onClick={() => add(null)}
        >
            {t('sheetEditor:addEmptyRow')}
        </Card>
    </Stack>;
}