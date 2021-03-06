package de.metanome.algorithms.hyfd.fixtures;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;

import de.metanome.algorithm_integration.ColumnCombination;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.input.InputIterationException;
import de.metanome.algorithm_integration.input.RelationalInput;
import de.metanome.algorithm_integration.result_receiver.ColumnNameMismatchException;
import de.metanome.algorithm_integration.result_receiver.CouldNotReceiveResultException;
import de.metanome.algorithm_integration.results.FunctionalDependency;

public class AlgorithmTestFixture1 extends AbstractAlgorithmTestFixture {
	
	public AlgorithmTestFixture1() throws CouldNotReceiveResultException {
		this.columnNames = ImmutableList.of("A", "B", "C", "D");
		this.numberOfColumns = 4;
		this.table.add(ImmutableList.of("1", "1", "0", "0"));
		this.table.add(ImmutableList.of("1", "2", "1", "4"));
		this.table.add(ImmutableList.of("3", "1", "3", "0"));
		this.table.add(ImmutableList.of("2", "2", "5", "4"));
		this.table.add(ImmutableList.of("1", "1", "0", "0"));
	}
	
	public RelationalInput getRelationalInput() throws InputIterationException {
		RelationalInput input = mock(RelationalInput.class);
		
		when(input.columnNames())
			.thenReturn(this.columnNames);
		when(Integer.valueOf(input.numberOfColumns()))
			.thenReturn(Integer.valueOf(this.numberOfColumns));
		when(input.relationName())
			.thenReturn(this.relationName);
		
		when(Boolean.valueOf(input.hasNext()))
			.thenReturn(Boolean.valueOf(true))
			.thenReturn(Boolean.valueOf(true))
			.thenReturn(Boolean.valueOf(true))
			.thenReturn(Boolean.valueOf(true))
			.thenReturn(Boolean.valueOf(true))
			.thenReturn(Boolean.valueOf(false));
		
		when(input.next())
			.thenReturn(this.table.get(0))
			.thenReturn(this.table.get(1))
			.thenReturn(this.table.get(2))
			.thenReturn(this.table.get(3))
			.thenReturn(this.table.get(4));
			
		return input;
	}

	public void verifyFunctionalDependencyResultReceiver() throws CouldNotReceiveResultException, ColumnNameMismatchException {
		ColumnIdentifier expA = new ColumnIdentifier(this.relationName, this.columnNames.get(0));
		ColumnIdentifier expB = new ColumnIdentifier(this.relationName, this.columnNames.get(1));
		ColumnIdentifier expC = new ColumnIdentifier(this.relationName, this.columnNames.get(2));
		ColumnIdentifier expD = new ColumnIdentifier(this.relationName, this.columnNames.get(3));
		
		verify(this.fdResultReceiver).receiveResult(new FunctionalDependency(new ColumnCombination(expC), expB));
		verify(this.fdResultReceiver).receiveResult(new FunctionalDependency(new ColumnCombination(expB), expD));
		verify(this.fdResultReceiver).receiveResult(new FunctionalDependency(new ColumnCombination(expC), expA));
		
		verify(this.fdResultReceiver).receiveResult(new FunctionalDependency(new ColumnCombination(expC), expD));
		verify(this.fdResultReceiver).receiveResult(new FunctionalDependency(new ColumnCombination(expD), expB));
		verify(this.fdResultReceiver).receiveResult(new FunctionalDependency(new ColumnCombination(expA, expB), expC));
		verify(this.fdResultReceiver).receiveResult(new FunctionalDependency(new ColumnCombination(expA, expD), expC));
		
		verifyNoMoreInteractions(this.fdResultReceiver);
	}
	
}
