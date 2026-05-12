import React, {useMemo} from 'react';
import {toIdMap} from '../../../utils/Utils';
import {ActionIcon, Button, Divider, Group, Stack, Text} from '@mantine/core';
import {arrayMove, SortableContext, useSortable, verticalListSortingStrategy} from '@dnd-kit/sortable';
import {closestCenter, DndContext} from '@dnd-kit/core';
import {CSS} from '@dnd-kit/utilities';
import {IconGripVertical, IconMinus, IconPlus} from '@tabler/icons-react';
import {useTranslation} from 'react-i18next';
import {ObjectSelect} from '../../../input/ObjectSelect';

/** Interface for needed attributes */
type Orderable = {
    /** The id of the object */
    id?: string;
    /** The name of the object */
    name: string;
}

/** A stack of the given elements which allows ordering */
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

    function handleDragEnd(event) {
        const {active, over} = event;

        if (active.id !== over.id) {
            const oldIndex = order.indexOf(active.id);
            const newIndex = order.indexOf(over.id);
            setOrder(arrayMove(order, oldIndex, newIndex));
        }
    }

    return <DndContext collisionDetection={closestCenter} onDragEnd={handleDragEnd}>
        <SortableContext items={order} strategy={verticalListSortingStrategy}>
            <Stack gap="xs">
                {order.map(option => (
                    <SortableLine
                        key={mappedList[option].id}
                        id={mappedList[option].id}
                        title={mappedList[option].name}
                    />
                ))}
            </Stack>
        </SortableContext>
    </DndContext>;
}

/** A stack of the given elements which allows ordering and adding and removing */
export function AddableOrderModifier<T extends Orderable>({
    order, setOrder, fullList
}: {
    order: string[]
    setOrder: (s: string[]) => void;
    fullList: T[];
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

    function handleDragEnd(event) {
        const {active, over} = event;

        if (active.id !== over.id) {
            const oldIndex = order.indexOf(active.id);
            const newIndex = order.indexOf(over.id);
            setOrder(arrayMove(order, oldIndex, newIndex));
        }
    }

    return <DndContext collisionDetection={closestCenter} onDragEnd={handleDragEnd}>
        <SortableContext items={order} strategy={verticalListSortingStrategy}>
            <Stack gap="xs">
                {order.map((option, index) => (
                    <SortableLine
                        key={option}
                        id={option}
                        title={mappedList[option]?.name ?? t('sheetEditor:emptyRow')}
                        extra={
                            <ActionIcon
                                size="sm"
                                color="red"
                                variant="outline"
                                onClick={() => remove(index)}
                            >
                                <IconMinus/>
                            </ActionIcon>
                        }
                    />
                ))}
                <Divider/>
                <ObjectSelect
                    data={fullList}
                    idKey="id"
                    labelKey="name"
                    onChange={o => add(o)}
                    rightSection={<IconPlus/>}
                    comboboxProps={{withinPortal: false}}
                />
                <Button onClick={() => {
                    add({id: 'empty-' + Date.now().toString(), name: ''} as T);
                }}>
                    {t('sheetEditor:addEmptyRow')}
                </Button>
            </Stack>
        </SortableContext>
    </DndContext>;
}

function SortableLine({
    id,
    title,
    extra
}: {
    id: string,
    title: string,
    extra?: React.ReactNode
}) {
    const {
        attributes,
        listeners,
        setNodeRef,
        transform,
        transition,
        isDragging
    } = useSortable({id});

    const style = {
        transform: CSS.Transform.toString(transform),
        transition,
        zIndex: isDragging ? 100 : 'auto',
        opacity: isDragging ? 0.5 : 1,
        width: '100%',
    };

    return <Group
        ref={setNodeRef}
        style={style}
        wrap="nowrap"
        p="xs"
    >
        <Text size="sm" style={{flex: 1}}>{title}</Text>
        {extra}
        <div {...attributes} {...listeners} style={{cursor: 'grab', display: 'flex'}}>
            <IconGripVertical size={18} color="gray"/>
        </div>
    </Group>;
}