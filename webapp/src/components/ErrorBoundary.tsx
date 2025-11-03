import React, {Component, ErrorInfo, ReactNode} from 'react';

/** Props for the error boundary */
interface ErrorBoundaryProps {
    /** The children of this boundary */
    children: ReactNode;
    /** The fallback shown if there is an error */
    fallback?: ReactNode;
}

/** State of the error boundary */
interface ErrorBoundaryState {
    /** If the boundary has caught an error */
    hasError: boolean;
    /** The possible error */
    error?: Error;
}

/** A boundary to catch errors */
export class ErrorBoundary extends Component<ErrorBoundaryProps, ErrorBoundaryState> {
    constructor(props: ErrorBoundaryProps) {
        super(props);
        this.state = {hasError: false};
    }

    /** Calculates the state from an error */
    static getDerivedStateFromError(error: Error): ErrorBoundaryState {
        return {hasError: true, error};
    }

    override componentDidCatch(error: Error, errorInfo: ErrorInfo) {
        this.setState(ErrorBoundary.getDerivedStateFromError(error));
        console.error('Uncaught error:', error, errorInfo);
    }


    override render() {
        if (this.state.hasError) {
            return this.props.fallback ?? (
                <div style={{padding: '1rem', color: 'red'}}>
                    <h2>Something went wrong.</h2>
                    <p>{this.state.error?.message}</p>
                </div>
            );
        }

        return this.props.children;
    }
}