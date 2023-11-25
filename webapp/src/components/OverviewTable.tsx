import { Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@mui/material";
import React, { Component, FC } from "react";

interface Column<T> {
    name: string,
    getter: (type: T) => string | number
}

interface OverviewTableProps<T> {
    data: T[]
    keyGetter: (t: T) => string
    columns: Column<T>[]
}

function OverviewTable<T>(props: React.PropsWithChildren<OverviewTableProps<T>>) {
    return (
        <TableContainer component={Paper}>
          <Table sx={{ minWidth: 650 }} size="small" aria-label="a dense table">
            <TableHead>
              <TableRow>
                {props.columns.map(column => (
                    <TableCell key={column.name}>{column.name}</TableCell>
                ))}
              </TableRow>
            </TableHead>
            <TableBody>
              {props.data.map((row) => (
                <TableRow
                  key={props.keyGetter(row)}
                  sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                >
                    {props.columns.map(column => (
                        <TableCell key={column.name}>{column.getter(row)}</TableCell>
                    ))}
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      );
}

export default OverviewTable