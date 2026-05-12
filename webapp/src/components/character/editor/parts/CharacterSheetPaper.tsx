import {ActionIcon, AspectRatio, Box, Paper} from '@mantine/core';
import React, {useContext} from 'react';
import {PnPCharacterSheetContext} from '../../PnPCharacterSheetContext';
import {Responsive, WidthProvider} from 'react-grid-layout';
import {PageElement, PageElementData, PageElementLayout} from './PageElement';
import {IconX} from '@tabler/icons-react';

const ResponsiveReactGridLayout = WidthProvider(Responsive);

const A4_WIDTH = 800;
const A4_RATIO = 1 / 1.4142;

/** A single page of a character sheet */
export type PnPCharacterSheetPage = {
    data: PageElementData[];
    layout: PageElementLayout[]
}

/** A single page of a character sheet */
export function CharacterSheetPaper({
    page,
    updatePage,
    pageNumber,
    activeDropSettings
}: {
    page: PnPCharacterSheetPage,
    updatePage: (page: Partial<PnPCharacterSheetPage>) => void,
    pageNumber: number,
    activeDropSettings?: Partial<PageElementLayout>
}) {
    const {allowEdit} = useContext(PnPCharacterSheetContext);

    return <AspectRatio
        ratio={A4_RATIO}
        style={{
            width: A4_WIDTH,
            maxWidth: '100%'
        }}
    >
        <Paper
            shadow="sm"
            p="md"
            withBorder
            className={'page-break ' + (pageNumber === 0 ? 'print-clean-first' : 'print-clean')}
            style={{
                overflow: 'hidden',
                height: '100%',
                width: '100%',
                padding: window.matchMedia('print').matches ? 0 : undefined
            }}
        >
            <div className="print-width-lock" style={{width: '100%', height: '100%'}}>
                <ResponsiveReactGridLayout
                    className={`layout ${!allowEdit ? 'layout-read-only' : ''}`}
                    measureBeforeMount={false}
                    style={{height: '100%'}}
                    autoSize={false}
                    layouts={{
                        lg: page.layout.map(item => ({
                            ...item,
                            static: !allowEdit
                        }))
                    }}
                    breakpoints={{lg: 0}}
                    cols={{lg: 12}}
                    rowHeight={(A4_WIDTH / A4_RATIO) / 60}
                    margin={[10, 10]}
                    onLayoutChange={(newLayout: PageElementLayout[]) => updatePage({layout: newLayout})}
                    compactType={null}
                    preventCollision={true}
                    draggableHandle={allowEdit ? '.drag-handle' : '.disabled-handle'}
                    draggableCancel={allowEdit ? '.no-drag' : null}
                    isDroppable={allowEdit}
                    onDrop={(layouts: PageElementLayout[], layout: PageElementLayout, event: React.DragEvent) => {
                        const dataString = event.dataTransfer.getData('design-element');
                        const customData: PageElementData = JSON.parse(dataString);

                        updatePage({
                            layout: [...layouts.filter(l => l.i !== '__dropping-elem__'), {
                                ...layout,
                                i: Date.now().toString()
                            }],
                            data: [...page.data, customData]
                        });
                    }}
                    droppingItem={activeDropSettings ? {
                        i: '__dropping-elem__',
                        ...activeDropSettings // Dynamically spreads w, h, minW, minH
                    } : null}
                    isDraggable={allowEdit}
                    isResizable={allowEdit}
                >
                    {page.data.map((item, index) => (
                        <div
                            key={page.layout[index].i}
                            className="drag-handle"
                            style={{
                                backgroundColor: '#fff',
                                border: '1px dashed #dee2e6',
                                borderRadius: '4px',
                                display: 'flex',
                                flexDirection: 'column',
                                position: 'relative', // Necessary for the absolute button to anchor here
                                cursor: 'grab',       // Visual cue that the whole area is draggable
                                overflow: 'hidden',   // Keeps content inside the rounded corners
                            }}
                        >
                            <Box
                                style={{
                                    position: 'absolute',
                                    top: 0,
                                    left: 0,
                                    right: 0,
                                    bottom: 0,
                                    zIndex: 0
                                }}
                            >
                                <PageElement
                                    data={item}
                                    setData={d => {
                                        updatePage({
                                            data: page.data.map((item, i) => index !== i ? item : d),
                                        });
                                    }}
                                />
                            </Box>
                            {allowEdit ?
                                <ActionIcon
                                    variant="subtle"
                                    color="red"
                                    size="sm"
                                    className="no-drag"
                                    onClick={(e) => {
                                        e.stopPropagation();
                                        updatePage({
                                            layout: page.layout.filter((_, i) => i !== index),
                                            data: page.data.filter((_, i) => i !== index)
                                        });
                                    }}
                                    style={{
                                        position: 'absolute',
                                        top: 2,
                                        right: 2,
                                        zIndex: 10, // Ensure it stays above everything
                                    }}
                                >
                                    <IconX size={14}/>
                                </ActionIcon> : null
                            }
                        </div>
                    ))}
                </ResponsiveReactGridLayout>
            </div>
        </Paper>
    </AspectRatio>;
}