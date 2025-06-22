import {AspectRatio, Paper} from "@mantine/core";
import React, {ReactNode, useContext} from "react";
import {StackPart} from "./layout/StackPart";
import {SheetEditorContext} from "../PnPCharacterSheetEditor";

/** A single page of a character sheet */
export const CharacterSheetPaper = ({
    pageNumber, children, ...props
}: {
    pageNumber: number;
    children?: ReactNode;
}) => {
    const {selectedPage} = useContext(SheetEditorContext);

    return <AspectRatio
        ratio={1 / 1.4142}
        w="800px"
        className={selectedPage !== pageNumber ? "page-hidden" : ""}
    >
        <Paper
            shadow="sm"
            p="md"
            withBorder
            className="page-break print-clean"
            {...props}
        >
            <StackPart>
                {children}
            </StackPart>
        </Paper>
    </AspectRatio>;
};

CharacterSheetPaper.craft = {
    name: "sheetEditor:page",
    rules: {
        canDelete: () => false
    }
};