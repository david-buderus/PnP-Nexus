import { Checkbox, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TablePagination, TableRow, TableSortLabel, TextField, Tooltip } from "@mui/material";
import React, { Dispatch, SetStateAction } from "react";

type Getter<T> = (type: T) => string | number

/** Description of a column */
interface Column<T> {
  /** Human-readable column label */
  label: string;
  /** Field of T represented by this column */
  id: keyof T;
  /** The result of the getter will be shown in the table cell */
  getter: Getter<T>
  /** If this field shows a number */
  numeric: boolean;
}

/** Description of a table */
interface OverviewTableProps<T> {
  /** Data shown in this tagble */
  data: T[];
  /** Field of T which identifies it uniquely */
  id: keyof T;
  /** Which key is used for default sorting */
  sortBy: keyof T;
  /** The columns shown in the table */
  columns: Column<T>[];
  /** Setter and getter to set/get the currently selcted data */
  selectedState: [readonly T[keyof T][], Dispatch<SetStateAction<readonly T[keyof T][]>>]
}

/** Sorting  order for the table */
type Order = 'asc' | 'desc';

/** Internal description of a table */
interface TableProps<T> {
   /** The columns shown in the table */
  columns: Column<T>[];
  /** The amount of selected items */
  numSelected: number;
  /** Eventhandler */
  onRequestSort: (event: React.MouseEvent<unknown>, property: keyof T) => void;
  /** Eventhandler */
  onSelectAllClick: (event: React.ChangeEvent<HTMLInputElement>) => void;
  /** How the items are orderd */
  order: Order;
  /** The key of T used to order the items */
  orderBy: keyof T;
  /** The number of rows shown */
  rowCount: number;
  /** Eventhandler */
  filterUpdate: (value: string, index: number) => void
}

function descendingComparator<T>(a: T, b: T, getter: Getter<T>) {
  if (getter(b) < getter(a)) {
    return -1;
  }
  if (getter(b) > getter(a)) {
    return 1;
  }
  return 0;
}

function getComparator<T>(
  order: Order,
  getter: Getter<T>
): (
  a: T,
  b: T,
) => number {
  return order === 'desc'
    ? (a, b) => descendingComparator(a, b, getter)
    : (a, b) => -descendingComparator(a, b, getter);
}

function OverviewTableHead<T>(props: React.PropsWithChildren<TableProps<T>>) {
  const { onSelectAllClick, columns, order, orderBy, numSelected, rowCount, onRequestSort, filterUpdate } = props;
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
              'aria-label': 'select all'
            }}
          />
        </TableCell>
        {columns.map((column, index) => 
          <TableCell
            key={column.id as string}
            align={column.numeric ? 'right' : 'left'}
            sortDirection={orderBy === column.id ? order : false}
          >
            <div className="flex">
              <Tooltip title={column.label} placement="bottom-start">
                <TextField
                  label={column.label}
                  size="small"
                  type="search"
                  variant="standard"
                  onChange={event => filterUpdate(event.target.value.toLowerCase(), index)}
                >
                </TextField>
              </Tooltip>
              <TableSortLabel
                active={orderBy === column.id}
                direction={orderBy === column.id ? order : 'asc'}
                onClick={createSortHandler(column.id)}
              >
              </TableSortLabel>
            </div>
          </TableCell>
        )}
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
  const [rowsPerPage, setRowsPerPage] = React.useState(25);
  const [filters, setFilters] = React.useState(Array(columns.length).fill(""));
  const dense = false; // I may want to change this to be toggleble

  const handleRequestSort = (
    event: React.MouseEvent<unknown>,
    property: keyof T
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

  const handleClick = (event: React.MouseEvent<unknown>, identifier: T[keyof T]) => {
    const selectedIndex = selected.indexOf(identifier);
    let newSelected: readonly T[keyof T][] = [];

    if (selectedIndex === -1) {
      newSelected = newSelected.concat(selected, identifier);
    } else if (selectedIndex === 0) {
      newSelected = newSelected.concat(selected.slice(1));
    } else if (selectedIndex === selected.length - 1) {
      newSelected = newSelected.concat(selected.slice(0, -1));
    } else if (selectedIndex > 0) {
      newSelected = newSelected.concat(
        selected.slice(0, selectedIndex),
        selected.slice(selectedIndex + 1)
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

  const isSelected = (identifier: T[keyof T]) => selected.indexOf(identifier) !== -1;

  // Avoid a layout jump when reaching the last page with empty rows.
  const emptyRows =
    page > 0 ? Math.max(0, (1 + page) * rowsPerPage - data.length) : 0;

  const visibleRows = React.useMemo(
    () => {
      const getter = columns.find(c => c.id === orderBy)?.getter;
      return data.filter(dataValue => columns.every((column, index) => {
        if (!filters[index]) {
          return true;
        }
        const fieldValue = column.getter(dataValue);
        if (column.numeric) {
          return fieldValue === filters[index];
        } else {
          return (fieldValue as string).toLowerCase().includes(filters[index]);
        }
      })).slice(
        page * rowsPerPage,
        page * rowsPerPage + rowsPerPage
      ).sort(getComparator(order, getter));
    },
    [order, orderBy, page, rowsPerPage, data, filters]
  );

  return (
    <Paper>
      <TableContainer sx={{ maxHeight: 0.8, minHeight: 0.8 }}>
        <Table sx={{ minWidth: 650 }} stickyHeader size="small" aria-label="a dense table">
          <OverviewTableHead
            columns={columns}
            numSelected={selected.length}
            order={order}
            orderBy={orderBy}
            onSelectAllClick={handleSelectAllClick}
            onRequestSort={handleRequestSort}
            rowCount={data.length}
            filterUpdate={(value, index) =>
              setFilters(filters.map((old, i) => {
                if (i === index) {
                  return value;
                }
                return old;
              }))
            }
          />
          <TableBody>
            {visibleRows.map((row, index) => {
              const isItemSelected = isSelected(row[id]);
              const labelId = `enhanced-table-checkbox-${index}`;

              return <TableRow
                hover
                onClick={(event) => { handleClick(event, row[id]); }}
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
                      'aria-labelledby': labelId
                    }}
                  />
                </TableCell>
                {columns.map(column => 
                  <TableCell
                    key={column.label}
                    align={column.numeric ? 'right' : 'left'}
                  >
                    {column.getter(row)}
                  </TableCell>
                )}
              </TableRow>;
            })}
            {emptyRows > 0 && 
              <TableRow
                style={{
                  height: (dense ? 33 : 53) * emptyRows
                }}
              >
                <TableCell colSpan={6} />
              </TableRow>
            }
          </TableBody>
        </Table>
      </TableContainer>
      <TablePagination
        rowsPerPageOptions={[5, 10, 25, 50, { value: -1, label: 'All' }]}
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

export default OverviewTable;
