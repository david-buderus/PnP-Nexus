import { Checkbox, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TablePagination, TableRow, TableSortLabel } from "@mui/material";
import React, { Dispatch, SetStateAction } from "react";

interface Column<T> {
  label: string;
  id: keyof T;
  getter: (type: T) => string | number
  numeric: boolean;
}

interface OverviewTableProps<T> {
  data: T[];
  id: keyof T;
  sortBy: keyof T;
  columns: Column<T>[];
  selectedState: [readonly T[keyof T][], Dispatch<SetStateAction<readonly T[keyof T][]>>]
}

interface TableProps<T> {
  columns: Column<T>[];
  numSelected: number;
  onRequestSort: (event: React.MouseEvent<unknown>, property: keyof T) => void;
  onSelectAllClick: (event: React.ChangeEvent<HTMLInputElement>) => void;
  order: Order;
  orderBy: keyof T;
  rowCount: number;
}

function descendingComparator<T>(a: T, b: T, getter: (type: T) => string | number) {
  if (getter(b) < getter(a)) {
    return -1;
  }
  if (getter(b) > getter(a)) {
    return 1;
  }
  return 0;
}

type Order = 'asc' | 'desc';

function getComparator<T>(
  order: Order,
  getter: (type: T) => string | number,
): (
  a: T,
  b: T,
) => number {
  return order === 'desc'
    ? (a, b) => descendingComparator(a, b, getter)
    : (a, b) => -descendingComparator(a, b, getter);
}

function OverviewTableHead<T>(props: React.PropsWithChildren<TableProps<T>>) {
  const { onSelectAllClick, columns, order, orderBy, numSelected, rowCount, onRequestSort } = props;
  const createSortHandler =
    (property: keyof T) => (event: React.MouseEvent<unknown>) => {
      onRequestSort(event, property);
    };

  return (
    <TableHead>
      <TableRow>
        <TableCell padding="checkbox">
          <Checkbox
            color="primary"
            indeterminate={numSelected > 0 && numSelected < rowCount}
            checked={rowCount > 0 && numSelected === rowCount}
            onChange={onSelectAllClick}
            inputProps={{
              'aria-label': 'select all',
            }}
          />
        </TableCell>
        {columns.map(column => (
          <TableCell
            key={column.id as string}
            align={column.numeric ? 'right' : 'left'}
            sortDirection={orderBy === column.id ? order : false}
          >
            <TableSortLabel
              active={orderBy === column.id}
              direction={orderBy === column.id ? order : 'asc'}
              onClick={createSortHandler(column.id)}
            >
              {column.label}
            </TableSortLabel>
          </TableCell>
        ))}
      </TableRow>
    </TableHead>
  );
}

function OverviewTable<T>(props: React.PropsWithChildren<OverviewTableProps<T>>) {
  const { id, sortBy, data, columns } = props;
  const [order, setOrder] = React.useState<Order>('asc');
  const [orderBy, setOrderBy] = React.useState<keyof T>(sortBy);
  const [selected, setSelected] = props.selectedState;
  const [page, setPage] = React.useState(0);
  const [dense, setDense] = React.useState(false);
  const [rowsPerPage, setRowsPerPage] = React.useState(5);

  const handleRequestSort = (
    event: React.MouseEvent<unknown>,
    property: keyof T,
  ) => {
    const isAsc = orderBy === property && order === 'asc';
    setOrder(isAsc ? 'desc' : 'asc');
    setOrderBy(property);
  };

  const handleSelectAllClick = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.checked) {
      const newSelected = data.map(n => n[id]);
      setSelected(newSelected);
      return;
    }
    setSelected([]);
  };

  const handleClick = (event: React.MouseEvent<unknown>, id: T[keyof T]) => {
    const selectedIndex = selected.indexOf(id);
    let newSelected: readonly T[keyof T][] = [];

    if (selectedIndex === -1) {
      newSelected = newSelected.concat(selected, id);
    } else if (selectedIndex === 0) {
      newSelected = newSelected.concat(selected.slice(1));
    } else if (selectedIndex === selected.length - 1) {
      newSelected = newSelected.concat(selected.slice(0, -1));
    } else if (selectedIndex > 0) {
      newSelected = newSelected.concat(
        selected.slice(0, selectedIndex),
        selected.slice(selectedIndex + 1),
      );
    }
    setSelected(newSelected);
  };

  const handleChangePage = (event: unknown, newPage: number) => {
    setPage(newPage);
  };

  const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement>) => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  const isSelected = (id: T[keyof T]) => selected.indexOf(id) !== -1;

  // Avoid a layout jump when reaching the last page with empty rows.
  const emptyRows =
    page > 0 ? Math.max(0, (1 + page) * rowsPerPage - data.length) : 0;

  const visibleRows = React.useMemo(
    () => {
      const getter = columns.find(c => c.id === orderBy)?.getter
      return data.slice(
        page * rowsPerPage,
        page * rowsPerPage + rowsPerPage
      ).sort(getComparator(order, getter))
    },
    [order, orderBy, page, rowsPerPage, data],
  );

  return (
    <Paper>
      <TableContainer>
        <Table sx={{ minWidth: 650 }} size="small" aria-label="a dense table">
          <OverviewTableHead
            columns={columns}
            numSelected={selected.length}
            order={order}
            orderBy={orderBy}
            onSelectAllClick={handleSelectAllClick}
            onRequestSort={handleRequestSort}
            rowCount={data.length}
          />
          <TableBody>
            {visibleRows.map((row, index) => {
              const isItemSelected = isSelected(row[id]);
              const labelId = `enhanced-table-checkbox-${index}`;

              return (<TableRow
                hover
                onClick={(event) => handleClick(event, row[id])}
                role="checkbox"
                aria-checked={isItemSelected}
                tabIndex={-1}
                key={row[id] as string}
                selected={isItemSelected}
                sx={{ cursor: 'pointer' }}
              >
                <TableCell padding="checkbox">
                  <Checkbox
                    color="primary"
                    checked={isItemSelected}
                    inputProps={{
                      'aria-labelledby': labelId,
                    }}
                  />
                </TableCell>
                {columns.map(column => (
                  <TableCell 
                    key={column.label}
                    align={column.numeric ? 'right' : 'left'}
                  >{column.getter(row)}</TableCell>
                ))}
              </TableRow>
              );
            })}
            {emptyRows > 0 && (
              <TableRow
                style={{
                  height: (dense ? 33 : 53) * emptyRows,
                }}
              >
                <TableCell colSpan={6} />
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
      <TablePagination
          rowsPerPageOptions={[5, 10, 25]}
          component="div"
          count={data.length}
          rowsPerPage={rowsPerPage}
          page={page}
          onPageChange={handleChangePage}
          onRowsPerPageChange={handleChangeRowsPerPage}
        />
    </Paper>
  );
}

export default OverviewTable