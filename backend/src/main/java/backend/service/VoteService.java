package backend.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.model.Vote;
import backend.repo.VoteRepository;

@Service
public class VoteService {
    @Autowired
    private VoteRepository voteRepository;

    public List<Vote> getAllVotes() {
        return voteRepository.findAll();
    }

    public Vote getVoteById(long id) {
        return voteRepository.findHouseById(id);
    }

    public void addVote(Vote vote) {
        voteRepository.save(vote);
    }

    // Add other methods as needed for specific operations on votes

    // For example:
    public List<Vote> getVoteByVoteable(boolean voteable) {
        return voteRepository.findByVoteable(voteable);
    }

    public boolean voteOnVoteable(Long id) {
        Optional<Vote> optionalVote = voteRepository.findById(id);
        if (optionalVote.isPresent()) {
            Vote vote = optionalVote.get();
            if (vote.getVoteable()) {
                // Increase the amount_of_votes by one
                vote.setAmount_of_votes(vote.getAmount_of_votes() + 1);
                voteRepository.save(vote);
                return true; // Vote successfully incremented
            }
        }
        return false; // Vote not found or not voteable
    }

    public Map<String, Integer> voteOnPreliminaries(List<Integer> ids) {
        // Implement your logic to vote on multiple non-voteable items here
        // The map should contain the item IDs and the corresponding votes recorded

        Map<String, Integer> results = new HashMap<>();
        for (int id : ids) {
            // Your logic to record votes for each item goes here
            // For example:
            Vote vote = addAVote(id);

            results.put(vote.getName(), vote.getAmount_of_votes());
        }

        return results;
    }

    public List<Vote> turnTopIntoVoteable(int x) {
        List<Vote> topXNonVoteables = voteRepository.findTopXNonVoteables(x);
        topXNonVoteables.forEach(vote -> {
            // Set voteable to true
            vote.setVoteable(true);
            // Reset amount_of_votes for the newly voteable items
            vote.setAmount_of_votes(0);
        });
        voteRepository.saveAll(topXNonVoteables);
        return topXNonVoteables;
    }

    public Vote addAVote(int id) {
        Vote vote = getVoteById(id);
        vote.setAmount_of_votes(vote.getAmount_of_votes() + 1);
        return vote;
    }
}
