/**
 * Page to show on a universe specific page if no universe has been selected.
 */
export function NoUniverse() {
    return <div className=" bott">
        <h1 className="text-3xl font-bold underline">
            No universe has been selected.
        </h1>
        <text>
            Please select a universe. If you cannot select a universe, you must be invited to a universe.
        </text>
    </div>;
}
